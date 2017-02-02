package software.com.findmenear.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import software.com.findmenear.activities.HomeActivity;
import software.com.findmenear.services.PlaceRequestorService;


public class PlaceReceiver extends BroadcastReceiver {
  private static final String TAG = PlaceReceiver.class.getSimpleName();

  private HomeActivity activityObserver;

  public PlaceReceiver(){

  }


  @Override
  public void onReceive(Context context, Intent intent) {

    if(activityObserver != null){
      activityObserver.printTitle(intent.getStringExtra(PlaceRequestorService.PLACE_TYPE));
      activityObserver.printPlaces(intent.getStringExtra(PlaceRequestorService.PLACE_JSON));
    }
  }


  public void registerHomeActivity(HomeActivity activity){

    this.activityObserver = activity;

  }
}
