package software.com.findmenear.utils;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.List;


public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {
  private static final String TAG = GetNearbyPlacesData.class.getSimpleName();

  GoogleMap mMap;
  String jsonData;

  @Override
  protected String doInBackground(Object... params) {
    try {
      Log.d(TAG, "doInBackground entered");
      mMap = (GoogleMap) params[0];
      jsonData = (String) params[1];


    } catch (Exception e) {
      Log.d(TAG, e.toString());
    }
    return jsonData;
  }

  @Override
  protected void onPostExecute(String result) {
    Log.d(TAG, "onPostExecute Entered");
    List<HashMap<String, String>> nearbyPlacesList = null;
    DataParser dataParser = new DataParser();
    nearbyPlacesList =  dataParser.parseJSONToList(result);

    Location currentPosition = dataParser.parseCurrentLocation(result);

    ShowNearestPlace(currentPosition, nearbyPlacesList);
    Log.d(TAG, "onPostExecute Exit");
  }

  // nel ciclo for, va fatto il controllo della distanza per il più vicino, per implementera
  //il punto più vicino, ad ora li stampa tutti quanti. quindi il ciclo lo utilizzo per
  //controllare il più vicino.
  private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
    mMap.clear();

    for (int i = 0; i < nearbyPlacesList.size(); i++) {
      Log.d(TAG,"Entered into showing locations");

      MarkerOptions markerOptions = new MarkerOptions();
      HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

      Log.d(TAG, "latitude: " + googlePlace.get("lat"));
      Log.d(TAG, "longitude: " + googlePlace.get("lng"));

      double lat = Double.parseDouble(googlePlace.get("lat"));
      double lng = Double.parseDouble(googlePlace.get("lng"));
      String placeName = googlePlace.get("place_name");
      String vicinity = googlePlace.get("vicinity");
      LatLng latLng = new LatLng(lat, lng);
      markerOptions.position(latLng);
      markerOptions.title(placeName + " : " + vicinity);
      mMap.addMarker(markerOptions);
      markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
      //move map camera
      mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
      mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
    }
  }

  private void ShowNearestPlace(Location currPos, List<HashMap<String, String>> nearbyPlacesList) {
    mMap.clear();


    Location pointToPrint = new Location("PointToPrint");
    MarkerOptions markerToPrint = null;
    float distance = 0;

    for (int i = 0; i < nearbyPlacesList.size(); i++) {
      Log.d(TAG,"Entered into showing locations");

      MarkerOptions markerOptions = new MarkerOptions();
      HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

      Log.d(TAG, "latitude: " + googlePlace.get("lat"));
      Log.d(TAG, "longitude: " + googlePlace.get("lng"));

      double lat = Double.parseDouble(googlePlace.get("lat"));
      double lng = Double.parseDouble(googlePlace.get("lng"));
      String placeName = googlePlace.get("place_name");
      String vicinity = googlePlace.get("vicinity");
      LatLng latLng = new LatLng(lat, lng);
      markerOptions.position(latLng);
      markerOptions.title(placeName + " : " + vicinity);
      markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

      if(markerToPrint == null){
        pointToPrint.setLatitude(latLng.latitude);
        pointToPrint.setLongitude(latLng.longitude);
        Log.d(TAG,"First Distance: " + distance);
        distance = pointToPrint.distanceTo(currPos);

        markerToPrint = markerOptions;

      } else {
        Location tmpPoint = new Location("PointToPrint");
        tmpPoint.setLatitude(latLng.latitude);
        tmpPoint.setLongitude(latLng.longitude);

        float lDistance = tmpPoint.distanceTo(currPos);
        Log.d(TAG,"Distance: " + lDistance);

        if(lDistance < distance){
          distance = lDistance;
          markerToPrint = markerOptions;
          pointToPrint = tmpPoint;
        }
      }
    }

    mMap.addMarker(markerToPrint);
    //move map camera
    mMap.moveCamera(CameraUpdateFactory.newLatLng(markerToPrint.getPosition()));
    mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
  }


}
