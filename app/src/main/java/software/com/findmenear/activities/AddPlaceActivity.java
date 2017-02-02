package software.com.findmenear.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import software.com.findmenear.R;
import software.com.findmenear.utils.PlaceMemoryManager;


public class AddPlaceActivity extends DrawerActivity {
  private static final String TAG = AddPlaceActivity.class.getSimpleName();

  private double latitude;
  private double longitude;


  private AppCompatSpinner spinner;
  private EditText titleText;
  private EditText descriptionText;

  PlaceMemoryManager placeMemoryManager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_place);

    spinner = (AppCompatSpinner)findViewById(R.id.category_spinner);
    titleText = (EditText)findViewById(R.id.placeNameText);
    descriptionText = (EditText)findViewById(R.id.placeDescriptionText);


    Intent intent = getIntent();
    if(intent != null){
      latitude = Double.parseDouble(intent.getStringExtra("latitude"));
      longitude = Double.parseDouble(intent.getStringExtra("longitude"));
    }

    placeMemoryManager = new PlaceMemoryManager(getApplicationContext());
  }


  public void discard(View view){
    finish();
  }


  public void save(View view){
    String category = spinner.getSelectedItem().toString();

    String message = "Saving message: " + titleText.getText().toString() + " " + descriptionText.getText().toString() + " " + latitude + " " + longitude;

    Log.d(TAG, message);
    placeMemoryManager.loadPlace(category);
    placeMemoryManager.savePlace(category, titleText.getText().toString(), descriptionText.getText().toString(), latitude, longitude);

    finish();
  }
}
