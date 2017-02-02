package software.com.findmenear.utils;

import android.location.Location;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class DataParser {



  public List<HashMap<String, String>> parseJSONToList(String jsonData) {
    JSONArray jsonArray = null;
    JSONObject jsonObject;

    try {
      Log.d("Places", "parseJSONToList");
      jsonObject = new JSONObject((String) jsonData);
      jsonArray = jsonObject.getJSONArray("results");
    } catch (JSONException e) {
      Log.d("Places", "parseJSONToList error");
      e.printStackTrace();
    }
    return getPlaces(jsonArray);
  }

  public Location parseCurrentLocation(String jsonData){
    JSONObject jsonObject;

    Location currPos = new Location("currentPosition");
    try {
      Log.d("Places", "parseCurrentLocation");
      jsonObject = new JSONObject((String) jsonData);
      double currLat = Double.parseDouble(jsonObject.getString("currLat"));
      double currLng = Double.parseDouble(jsonObject.getString("currLng"));

      currPos.setLatitude(currLat);
      currPos.setLongitude(currLng);

    } catch (JSONException e) {
      Log.d("Places", "parseJSONToList error");
      e.printStackTrace();
    }

    return currPos;
  }

  private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
    int placesCount = jsonArray.length();
    List<HashMap<String, String>> placesList = new ArrayList<>();
    HashMap<String, String> placeMap = null;
    Log.d("Places", "getPlaces");

    for (int i = 0; i < placesCount; i++) {
      try {
        placeMap = getPlace((JSONObject) jsonArray.get(i));
        if(placeMap != null) {
          placesList.add(placeMap);
        }
        Log.d("Places", "Adding places");

      } catch (JSONException e) {
        Log.d("Places", "Error in Adding places");
        e.printStackTrace();
      }
    }
    return placesList;
  }

  private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
    HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
    String placeName = "-NA-";
    String vicinity = "-NA-";
    String latitude = "";
    String longitude = "";
    String reference = "";

    Log.d("getPlace", "Entered");

    try {
      if (!googlePlaceJson.isNull("name")) {

        placeName = googlePlaceJson.getString("name");

      }else if (!googlePlaceJson.isNull("place_name")){

        placeName = googlePlaceJson.getString("place_name");

      }

        if (!googlePlaceJson.isNull("vicinity")) {
          vicinity = googlePlaceJson.getString("vicinity");
        }

        if (!googlePlaceJson.isNull("geometry")) {

          latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
          longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");

        } else {
          latitude = googlePlaceJson.getString("lat");
          longitude = googlePlaceJson.getString("lng");
        }
        reference = googlePlaceJson.getString("reference");

        googlePlaceMap.put("place_name", placeName);
        googlePlaceMap.put("vicinity", vicinity);
        googlePlaceMap.put("lat", latitude);
        googlePlaceMap.put("lng", longitude);
        googlePlaceMap.put("reference", reference);
        Log.d("getPlace", "Putting Places");

    } catch (JSONException e) {
      Log.d("getPlace", "Error");
      e.printStackTrace();
    }
    return googlePlaceMap;
  }



  public String parseListToJSON(List<HashMap<String, String>> places){

    String targetJSON = "";
    JSONObject mainObject = new JSONObject();
    JSONArray mainArray = new JSONArray();

    for (HashMap<String, String> place : places) {
      JSONObject obj = new JSONObject();

      for (HashMap.Entry<String, String> entry : place.entrySet())
      {
        try{

          obj.put(entry.getKey(), entry.getValue());

        }catch(JSONException e){
          e.printStackTrace();;
        }
      }

      mainArray.put(obj);
    }


    try{

      mainObject.put("results",mainArray);

    }catch(JSONException e){
      e.printStackTrace();;
    }


    targetJSON = mainObject.toString();

    return targetJSON;
  }

  public String parseListToJSON(String curLat, String currLng, List<HashMap<String, String>> places){

    String targetJSON = "";
    JSONObject mainObject = new JSONObject();
    JSONArray mainArray = new JSONArray();

    for (HashMap<String, String> place : places) {
      JSONObject obj = new JSONObject();

      for (HashMap.Entry<String, String> entry : place.entrySet())
      {
        try{

          obj.put(entry.getKey(), entry.getValue());

        }catch(JSONException e){
          e.printStackTrace();;
        }
      }

      mainArray.put(obj);
    }


    try{

      mainObject.put("currLat",curLat);
      mainObject.put("currLng",currLng);
      mainObject.put("results",mainArray);

    }catch(JSONException e){
      e.printStackTrace();;
    }


    targetJSON = mainObject.toString();

    return targetJSON;
  }

}