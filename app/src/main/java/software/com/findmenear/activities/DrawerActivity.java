package software.com.findmenear.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import software.com.findmenear.R;
import software.com.findmenear.adapters.ToolbarAdapter;
import software.com.findmenear.services.PlaceRequestorService;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;


public class DrawerActivity extends AppCompatActivity {

  // Titoli della navigation Drawer con le icone corrispondenti.
  String TITLES[] = {"Restaurant", "School", "Hospital","Airport", "Train Station", "Atm", "Museum", "University", "Parking", "Add Place"};
  int ICONS[] = {R.drawable.ic_home, R.drawable.ic_calendar,
                 R.drawable.ic_message, R.drawable.ic_message,
                 R.drawable.ic_message, R.drawable.ic_message,
                 R.drawable.ic_message, R.drawable.ic_message,
                 R.drawable.ic_message, R.drawable.ic_message };

  // Header details
  String NAME = "Daniele Pizzi";
  String EMAIL = "Pizzi.daniele1993@gmail.com";
  int PROFILE = R.drawable.profile_pic;

  private Toolbar toolbar;                              // Dichiarazione della toolbar

  RecyclerView mRecyclerView;                           // Dichiarazione RecyclerView
  RecyclerView.Adapter mAdapter;                        // Dichiarazione Adapter per Recycler View
  RecyclerView.LayoutManager mLayoutManager;            // Dichiarazione Layout Manager come linear layout manager
  DrawerLayout drawer;                                  // Dichiarazione DrawerLayout

  ActionBarDrawerToggle mDrawerToggle;                  // Dichiarazione Action Bar drawer Toggle

  Boolean doubleBackToExitPressedOnce = false;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }



  @Override
  public void setContentView(final int layoutResID) {
    DrawerLayout fullLayout = (DrawerLayout) getLayoutInflater()
        .inflate(R.layout.activity_drawer, null);  // Inflating the drawer layout.

    LinearLayout actContent = (LinearLayout) fullLayout.findViewById(R.id.content);

    // assegnazione della toolbar object alla view e settaggio della Action bar

    toolbar = (Toolbar) fullLayout.findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);


    mRecyclerView = (RecyclerView) fullLayout.findViewById(R.id.RecyclerView); // Assegnazione della RecyclerView Object all' xml View

    mRecyclerView.setHasFixedSize(true);       // gli oggetti della lista sono di dimensione fissa

    mAdapter = new ToolbarAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE, this);       // Creazione dell'adapter MyAdapter class
    // in cui passo titolo, icone, header view name, header view email, and header view profile picture

    mRecyclerView.setAdapter(mAdapter);                              // settaggio dell'adapter al RecyclerView

    final GestureDetector mGestureDetector = new GestureDetector(DrawerActivity.this, new GestureDetector.SimpleOnGestureListener() {

      @Override
      public boolean onSingleTapUp(MotionEvent e) {
        return true;
      }
    });

    mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
      @Override
      public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
        View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());

        if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
          drawer.closeDrawers();

          switch (recyclerView.getChildPosition(child)) {
            case 0:
              // questio gestisce il click sul menu per l'intestazione.
              break;
            case 1:
              // questo cattura il caso di navigazione nel Navigation Drawer.
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "restaurant");
              break;
            case 2:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "school");
              break;
            case 3:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "hospital");
              break;
            case 4:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "airport");
              break;
            case 5:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "train");
              break;
            case 6:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "atm");
              break;
            case 7:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "museum");
              break;
            case 8:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "university");
              break;
            case 9:
              PlaceRequestorService.startActionPlace(getApplicationContext(), latitude, longitude, "10000", "parking");
              break;
            case 10:
              Intent addPlaceActivityIntent = new Intent(getApplicationContext(), AddPlaceActivity.class);
              addPlaceActivityIntent.putExtra("latitude", latitude);
              addPlaceActivityIntent.putExtra("longitude", longitude);
              startActivity(addPlaceActivityIntent);
              break;
          }
          return true;
        }
        return false;
      }

      @Override
      public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
      }

      @Override
      public void onRequestDisallowInterceptTouchEvent(boolean b) {
      }
    });


    mLayoutManager = new LinearLayoutManager(this);                 // Creazione del layout Manager

    mRecyclerView.setLayoutManager(mLayoutManager);                 // Settaggio del layout Manager

    drawer = (DrawerLayout) fullLayout.findViewById(R.id.drawer_layout);        // disegna l'oggetto Assegnato alla view
    mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open_drawer, R.string.close_drawer) {

      @Override
      public void onDrawerOpened(View drawerView) {
        super.onDrawerOpened(drawerView);
        // qui il codice viene eseguito quando il drawer è aperto
      }

      @Override
      public void onDrawerClosed(View drawerView) {
        super.onDrawerClosed(drawerView);
        // il codice qui viene eseguito quando invece il drawer viene chiuso
      }
    }; //
    drawer.setDrawerListener(mDrawerToggle); // drawer Listener impostato sul drawer toggle
    mDrawerToggle.syncState();               // settaggio del drawer toggle sync State

    getLayoutInflater().inflate(layoutResID, actContent, true);
    super.setContentView(fullLayout);
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // creazione del menu;  questo aggiunge elementi alla barra delle operazioni , se presente.
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();


    if (id == R.id.action_settings) {
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onBackPressed() {

    // Back Button, chiude il drawer se è aperto, altrimenti viene premuto il tasto indietro.
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {

      // premere di nuovo il pulsante per chiudere
      if (doubleBackToExitPressedOnce) {
        super.onBackPressed();
        return;
      }
      this.doubleBackToExitPressedOnce = true;
      Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
      new Handler().postDelayed(new Runnable() {
        @Override
        public void run() {
          doubleBackToExitPressedOnce = false;
        }
      }, 2000);  // Dopo 2 secondi, il back button viene resettato. Se l'utente fa clic indietro due volte in meno di 2 secondi , l'applicazione eseguirà l'azione.
    }
  }


  //-- Private Methods ---------------------------------------------------
  private String latitude;
  private String longitude;
  private String proximityRadius;

  protected void setLocationInformation(Location location){
    latitude = Double.toString(location.getLatitude());
    longitude = Double.toString(location.getLongitude());
  }

  private void loadTitles(){
    SharedPreferences prefs = getSharedPreferences("FindMeNear_pref", Context.MODE_PRIVATE);
    int sizeArray = prefs.getInt("titles_array_size", 0);
    if(sizeArray == 0){
      saveTitles();
    }else{
      TITLES = new String[sizeArray];

      for(int i=0; i<sizeArray; i++)
        TITLES[i] = prefs.getString("titles_array_" + i, null);
    }

  }

  private void loadIcons(){
    SharedPreferences prefs = getSharedPreferences("FindMeNear_pref", Context.MODE_PRIVATE);
    int sizeArray = prefs.getInt("icons_array_size", 0);
    if(sizeArray == 0){
      saveIcons();
    }else{
      ICONS = new int[sizeArray];
      for(int i=0; i<sizeArray; i++)
        ICONS[i] = prefs.getInt("icons_array_" + i, R.drawable.ic_message);
    }
  }

  private void saveTitles(){

    SharedPreferences prefs = getSharedPreferences("FindMeNear_pref", Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = prefs.edit();
    edit.putInt("titles_array_size", TITLES.length);
    for(int i=0; i< TITLES.length; i++)
      edit.putString("titles_array_" + i, TITLES[i]);
    edit.commit();

  }

  private void saveIcons(){
    SharedPreferences prefs = getSharedPreferences("FindMeNear_pref", Context.MODE_PRIVATE);
    SharedPreferences.Editor edit = prefs.edit();
    edit.putInt("icons_array_size", ICONS.length);
    for(int i=0; i< ICONS.length; i++)
      edit.putInt("icons_array_" + i, ICONS[i]);
    edit.commit();
  }
}
