package software.com.findmenear.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PlaceMemoryManager {
  private static final String TAG = PlaceMemoryManager.class.getSimpleName();


  private Context context;
  private String[] place_title;
  private String[] place_descriptions;
  private double[] place_latitude;
  private double[] place_longitude;

  SharedPreferences prefs;

  private List<HashMap<String, String>> placeList;

  public PlaceMemoryManager(Context context) {
    this.context = context;
    prefs = context.getSharedPreferences("FindMeNear_pref", Context.MODE_PRIVATE);
    placeList = new ArrayList<>();
  }

  public void savePlace(String category, String title, String description, double latitude, double longitude){
    int arraySize = place_title.length;
    category = category.toLowerCase();

    //Putting the new item at ends of array
    place_title[arraySize - 1] = title;
    place_descriptions[arraySize - 1] = description;
    place_latitude[arraySize - 1] = latitude;
    place_longitude[arraySize - 1] = longitude;

    SharedPreferences.Editor edit = prefs.edit();
    edit.putInt(category + "_array_size", arraySize);

    for(int i=0; i< arraySize; i++) {

      Log.d(TAG, "Saving in pos " + i + "category: " + category);
      Log.d(TAG, "Saving in pos " + i + "title: " + place_title[i]);
      Log.d(TAG, "Saving in pos " + i + "description: " + place_descriptions[i]);
      Log.d(TAG, "Saving in pos " + i + "lat: " + place_latitude[i]);
      Log.d(TAG, "Saving in pos " + i + "long: " + place_longitude[i]);


      edit.putString(category + "_title_array_" + i, place_title[i]);
      edit.putString(category + "_descriptions_array_" + i, place_descriptions[i]);
      edit.putString(category + "_latitude_array_" + i, Double.toString(place_latitude[i]));
      edit.putString(category + "_longitude_array_" + i, Double.toString(place_longitude[i]));

    }

    edit.commit();

  }


  //Load all saved place of given category
  public int loadPlace(String category ){
    category = category.toLowerCase();
    int sizeArray = prefs.getInt(category + "_array_size", 0);

    if(sizeArray == 0){

      place_title = new String[1];
      place_descriptions = new String[1];
      place_latitude = new double[1];
      place_longitude = new double[1];

    }else{
      place_title = new String[sizeArray + 1];
      place_descriptions = new String[sizeArray + 1];
      place_latitude = new double[sizeArray + 1];
      place_longitude = new double[sizeArray + 1];

      for(int i=0; i < sizeArray; i++) {
        place_title[i] = prefs.getString(category + "_title_array_" + i, null);
        place_descriptions[i] = prefs.getString(category + "_descriptions_array_" + i, null);
        place_latitude[i] = Double.parseDouble(prefs.getString(category + "_latitude_array_" + i, "0.0"));
        place_longitude[i] = Double.parseDouble(prefs.getString(category + "_longitude_array_" + i, "0.0"));
      }

    }
    return sizeArray;
  }


  public String getPlaceList(String category){
    category = category.toLowerCase();
    Log.d(TAG, "Loading place list of category: " + category);
    int placeLoaded = loadPlace(category);
    Log.d(TAG, "Returning place: " + placeLoaded);
    //placeList = new ArrayList<>();
    JSONArray plcArray = new JSONArray();

    for(int i = 0; i < placeLoaded; i++){
      //HashMap<String, String> place = new HashMap<>();
      JSONObject plcObject = new JSONObject();
      JSONObject geometry = new JSONObject();
      JSONObject location = new JSONObject();

      try {

        plcObject.put("place_name", place_title[i]);
        plcObject.put("vicinity", place_descriptions[i]);
        location.put("lat", place_latitude[i]);
        location.put("lng", place_longitude[i]);

        geometry.put("location", location);

        plcObject.put("geometry", geometry);
        plcObject.put("reference", place_descriptions[i]);

      } catch (JSONException e) {
        e.printStackTrace();
      }

      //placeList.add(place);
      plcArray.put(plcObject);
    }
    JSONObject objToReturn = new JSONObject();
    try {

      objToReturn.put("results", plcArray);

    } catch (JSONException e) {
      e.printStackTrace();
    }

    Log.d(TAG, "Returning place: " + objToReturn.toString());
    return objToReturn.toString();
  }


}
