package software.com.findmenear.services;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import software.com.findmenear.utils.DataParser;
import software.com.findmenear.utils.DownloadUrl;
import software.com.findmenear.utils.PlaceMemoryManager;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class PlaceRequestorService extends IntentService {

  private static final String TAG = PlaceRequestorService.class.getSimpleName();

  // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
  private static final String ACTION_PLACE = "com.software.FindMeNearAndroid.services.action.PLACE";
  public static final String ACTION_PLACE_RESPONSE = "com.software.FindMeNearAndroid.services.action.PLACE_RESPONSE";



  private static final String LATITUDE = "com.software.FindMeNearAndroid.services.extra.LATITUDE";
  private static final String LONGITUDE = "com.software.FindMeNearAndroid.services.extra.LONGITUDE";
  private static final String PROXIMITY_RADIUS = "com.software.FindMeNearAndroid.services.extra.PROXIMITY_RADIUS";
  public static final String PLACE_TYPE = "com.software.FindMeNearAndroid.services.extra.PLACE_TYPE";
  public static final String PLACE_JSON = "com.software.FindMeNearAndroid.services.extra.PLACE_JSON";

  public PlaceRequestorService() {
    super("PlaceRequestorService");
  }

  String latitude;
  String longitude;
  String proximityRadius;
  String placeType;

  /**
   * Starts this service to perform action Foo with the given parameters. If
   * the service is already performing a task this action will be queued.
   *
   * @see IntentService
   */
  public static void startActionPlace(Context context, String latitude, String longitude, String proximityRadius, String placeType) {
    Intent intent = new Intent(context, PlaceRequestorService.class);

    intent.setAction(ACTION_PLACE);
    intent.putExtra(LATITUDE, latitude);
    intent.putExtra(LONGITUDE, longitude);
    intent.putExtra(PROXIMITY_RADIUS, proximityRadius);
    intent.putExtra(PLACE_TYPE, placeType);

    context.startService(intent);
  }


  @Override
  protected void onHandleIntent(Intent intent) {
    if (intent != null) {
      final String action = intent.getAction();

      if (ACTION_PLACE.equals(action)) {
        Log.d(TAG, "Action place Intent called");
        latitude = intent.getStringExtra(LATITUDE);
        longitude = intent.getStringExtra(LONGITUDE);
        proximityRadius = intent.getStringExtra(PROXIMITY_RADIUS);
        placeType = intent.getStringExtra(PLACE_TYPE);

        handleActionPlace(latitude, longitude, proximityRadius, placeType);
      }

    }
  }

  /**
   * Handle action Foo in the provided background thread with the provided
   * parameters.
   */
  private void handleActionPlace(String latitude, String longitude, String proximityRadius, String placeType) {

    String url = getUrl(latitude, longitude, proximityRadius, placeType);
    DownloadUrl downloadUrl = new DownloadUrl();

    List<HashMap<String, String>> googleNearbyPlacesList = null;
    List<HashMap<String, String>> localNearbyPlacesList = null;
    String googlePlacesData = "";
    String localPlacesData = "";

    try {
      googlePlacesData = downloadUrl.readUrl(url);

    } catch (IOException e) {
      e.printStackTrace();
    }


    PlaceMemoryManager placeMemoryManager = new PlaceMemoryManager(getApplicationContext());
    localPlacesData = placeMemoryManager.getPlaceList(placeType);

    /*Estracting result from remote and local and merge it*/
    DataParser dataParser = new DataParser();
    googleNearbyPlacesList =  dataParser.parseJSONToList(googlePlacesData);
    localNearbyPlacesList =  dataParser.parseJSONToList(localPlacesData);

    googleNearbyPlacesList.addAll(localNearbyPlacesList);

    String responceToSend = dataParser.parseListToJSON(latitude, longitude, googleNearbyPlacesList);

    sendPlaceResponse(responceToSend);
  }

  private String getUrl(String latitude, String longitude,String proximityRadius, String nearbyPlace) {

    StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
    googlePlacesUrl.append("location=" + latitude + "," + longitude);
    googlePlacesUrl.append("&radius=" + proximityRadius);
    googlePlacesUrl.append("&type=" + nearbyPlace);
    googlePlacesUrl.append("&sensor=true");
    googlePlacesUrl.append("&key=" + "AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0");
    Log.d("getUrl", googlePlacesUrl.toString());
    return (googlePlacesUrl.toString());
  }

  // creo un messaggio broadcast con una specifica tipologia di messaggio, con una action specifica
  // e dentro gli metto il risultato.
  private void sendPlaceResponse(String response){

    Log.d(TAG, "sending response:" + response);

    Intent intent = new Intent();
    intent.setAction(ACTION_PLACE_RESPONSE);
    intent.putExtra(PLACE_JSON, response);
    intent.putExtra(PLACE_TYPE, placeType);
    sendBroadcast(intent);

  }
}