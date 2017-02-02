package software.com.findmenear.activities;

import software.com.findmenear.R;
import software.com.findmenear.receivers.PlaceReceiver;
import software.com.findmenear.services.PlaceRequestorService;
import software.com.findmenear.utils.GetNearbyPlacesData;

import android.Manifest;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

public class HomeActivity extends DrawerActivity
    implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {

  private static final String TAG = HomeActivity.class.getSimpleName();

  private IntentFilter placeFilter;
  private PlaceReceiver placeReceiver;

  private GoogleApiClient mGoogleApiClient;

  //Location variable
  private Location mLastLocation;
  private LocationRequest mLocationRequest;

  private long UPDATE_INTERVAL = 60000;  /* 60 secs */
  private long FASTEST_INTERVAL = 5000; /* 5 secs */

  /*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
  private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


  //Maps variable
  private MapFragment mapFragment;
  private GoogleMap mMap;
  //private Marker mCurrLocationMarker;

  //TextView tmpText;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);

    //tmpText = (TextView) findViewById(R.id.tmp_text_view);

    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      checkLocationPermission();
    }

    //Check if Google Play Services Available or not
    if (!CheckGooglePlayServices()) {
      Log.d(TAG, "onCreate() Finishing test case since Google Play Services are not available");
      finish();
    } else {
      Log.d(TAG, " onCreate() Google Play Services available.");
    }

    buildGoogleApiClient();

    // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    mapFragment = (MapFragment) getFragmentManager()
        .findFragmentById(R.id.map);

    if(mapFragment == null){
      Log.d(TAG, " Map Fragment is null.");
    }else {
      Log.d(TAG, " Map Fragment is not null.");
      mapFragment.getMapAsync(this);
    }

    setupPlaceReceiver();
  }

  @Override
  public void onResume() {
    super.onResume();
    // Registra broadcast receiver.
    registerReceiver(placeReceiver, placeFilter);
    if(mGoogleApiClient != null)
        mGoogleApiClient.connect();
  }

  @Override
  public void onPause() {
    // annulla the receiver
    unregisterReceiver(placeReceiver);
    mGoogleApiClient.disconnect();
    super.onPause();
  }


  //-- Public methods ------------------------------------------------------------------------------
  public void printMessage(String str) {
    //tmpText.setText(str);
  }

  public void printTitle(String str){
    String title = str.substring(0, 1).toUpperCase() + str.substring(1);
    getSupportActionBar().setTitle(title);
  }


  public void printPlaces(String response){
    Object[] DataTransfer = new Object[3];
    DataTransfer[0] = mMap;
    DataTransfer[1] = response;

    GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
    getNearbyPlacesData.execute(DataTransfer);
  }

  //-- Private methods -----------------------------------------------------------------------------

  // riceve il messaggio del service, con la tipologia di messaggio ricevuti da quel service
  private void setupPlaceReceiver() {
    placeFilter = new IntentFilter(PlaceRequestorService.ACTION_PLACE_RESPONSE);
    placeReceiver = new PlaceReceiver();
    placeReceiver.registerHomeActivity(this);
  }

  protected synchronized void buildGoogleApiClient() {
    mGoogleApiClient = new GoogleApiClient.Builder(this)
        .addConnectionCallbacks(this)
        .addOnConnectionFailedListener(this)
        .addApi(LocationServices.API)
        .build();
    mGoogleApiClient.connect();
  }

  //-- Map methods ---------------------------------------------------------------------------------
  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

    //Inizializza il Google Play Services

    if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (ContextCompat.checkSelfPermission(this,
          Manifest.permission.ACCESS_FINE_LOCATION)
          == PackageManager.PERMISSION_GRANTED) {
        //buildGoogleApiClient();
        mMap.setMyLocationEnabled(true);
      }
    }
    else {
      //buildGoogleApiClient();
      mMap.setMyLocationEnabled(true);
    }

  }

  //-- Permission and connession checking methods --------------------------------------------------
  private boolean CheckGooglePlayServices() {
    GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
    int result = googleAPI.isGooglePlayServicesAvailable(this);
    if (result != ConnectionResult.SUCCESS) {
      if (googleAPI.isUserResolvableError(result)) {
        googleAPI.getErrorDialog(this, result,
            0).show();
      }
      return false;
    }
    return true;
  }

  public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

  public boolean checkLocationPermission() {
    if (ContextCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED) {

      // Asking user if explanation is needed
      if (ActivityCompat.shouldShowRequestPermissionRationale(this,
          Manifest.permission.ACCESS_FINE_LOCATION)) {

        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.

        //Prompt the user once explanation has been shown
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            MY_PERMISSIONS_REQUEST_LOCATION);


      } else {
        // No explanation needed, we can request the permission.
        ActivityCompat.requestPermissions(this,
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
            MY_PERMISSIONS_REQUEST_LOCATION);
      }
      return false;
    } else {
      return true;
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode,
                                         String permissions[], int[] grantResults) {
    switch (requestCode) {
      case MY_PERMISSIONS_REQUEST_LOCATION: {
        // If request is cancelled, the result arrays are empty.
        if (grantResults.length > 0
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

          // permission was granted. Do the
          // contacts-related task you need to do.

          if (ContextCompat.checkSelfPermission(this,
              Manifest.permission.ACCESS_FINE_LOCATION)
              == PackageManager.PERMISSION_GRANTED) {

            if (mGoogleApiClient == null) {
              buildGoogleApiClient();
            }
            mMap.setMyLocationEnabled(true);

          }

        } else {

          // Permission denied, Disable the functionality that depends on this permission.
          Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
        }

        return;
      }

      // other 'case' lines to check for other permissions this app might request.
      // You can add here other case statements according to your requirement.
    }
  }


  //-- Location listner methods --------------------------------------------------------------------

  @Override
  public void onConnected(Bundle bundle) {

    Log.d(TAG, "OnConnected()");

    if(checkLocationPermission())
      mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

    if (mLastLocation != null) {
      //tmpText.setText("Location: latitude " + mLastLocation.getLatitude() + " longitude: " + mLastLocation.getLongitude());
      super.setLocationInformation(mLastLocation);
    }

    startLocationUpdates();
  }

  protected void startLocationUpdates() {
    mLocationRequest = new LocationRequest();
    mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
    mLocationRequest.setInterval(UPDATE_INTERVAL);
    mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

    if(checkLocationPermission()){
      LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
    }

  }

  @Override
  public void onConnectionSuspended(int i) {

    if (i == CAUSE_SERVICE_DISCONNECTED) {
      Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
    } else if (i == CAUSE_NETWORK_LOST) {
      Toast.makeText(this, "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
    }

  }

  @Override
  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

		 // Google Play services can resolve some errors it detects. If the error
		 // has a resolution, try sending an Intent to start a Google Play
		 // services activity that can resolve error.

    if (connectionResult.hasResolution()) {
      try {
        // Start an Activity that tries to resolve the error
        connectionResult.startResolutionForResult(this,
            CONNECTION_FAILURE_RESOLUTION_REQUEST);

				 // Thrown if Google Play services canceled the original
				 // PendingIntent

      } catch (IntentSender.SendIntentException e) {
        // Log the error
        e.printStackTrace();
      }
    } else {
      Toast.makeText(getApplicationContext(),
          "Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
    }
  }

  @Override
  public void onLocationChanged(Location location) {
    mLastLocation = location;
    //tmpText.setText("Location: latitude " + mLastLocation.getLatitude() + " longitude: " + mLastLocation.getLongitude());

    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    //MarkerOptions markerOptions = new MarkerOptions();
    //markerOptions.position(latLng);
    //markerOptions.title("Current Position");
    //markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
    //mCurrLocationMarker = mMap.addMarker(markerOptions);

    //move map camera
    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    //Toast.makeText(HomeActivity.this,"Your Current Location", Toast.LENGTH_LONG).show();

    Log.d(TAG, String.format("latitude:%.3f longitude:%.3f", latLng.latitude, latLng.longitude));

    super.setLocationInformation(mLastLocation);
  }
}
