package in.eloksolutions.ayyappaapp.maps;

/**
 * Created by Vishnu on 9/30/2017.
 */

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;


import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.CardViewActivity;
import in.eloksolutions.ayyappaapp.activities.GroupList;
import in.eloksolutions.ayyappaapp.activities.OwnerView;
import in.eloksolutions.ayyappaapp.activities.PadiPoojaFull;
import in.eloksolutions.ayyappaapp.activities.UserView;
import in.eloksolutions.ayyappaapp.beans.UserDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.BottomNavigationViewHelper;
import in.eloksolutions.ayyappaapp.helper.GetUserNearMeTask;
import in.eloksolutions.ayyappaapp.util.Util;

/**
 * An activity that displays a Google map with a marker (pin) to indicate a particular location.
 */
public class MapsMarkerActivity extends AppCompatActivity
        implements OnMapReadyCallback,GoogleMap.OnMarkerClickListener,GoogleMap.OnInfoWindowClickListener {
    private static final String TAG="MapsMarkerActivity";
    GoogleMap googleMap;
    String sUserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the content view that renders the map.
        setContentView(R.layout.map_view);
/*        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Swamies Near to You");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
        // Get the SupportMapFragment and request notification
        // when the map is ready to be used.
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        sUserId=preferences.getString("userId",null);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        GetUserNearMeTask task=new GetUserNearMeTask("http://13.59.69.182/AS/user/loc/"+sUserId,this);
        task.execute();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(3);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home1:
                        Intent intent1 = new Intent(MapsMarkerActivity.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:
                        Intent intent2 = new Intent(MapsMarkerActivity.this, GroupList.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_books:
                        Intent intent3 = new Intent(MapsMarkerActivity.this, PadiPoojaFull.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_center_focus:

                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(MapsMarkerActivity.this, OwnerView.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }

    /**
     * Manipulates the map when it's available.
     * The API invokes this callback when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user receives a prompt to install
     * Play services inside the SupportMapFragment. The API invokes this method after the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng sydney = new LatLng(-33.852, 151.211);
        this.googleMap=googleMap;
        Marker marker=googleMap.addMarker(new MarkerOptions().position(sydney)
                .title("Marker in Sydney"));

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
       // googleMap.setOnMarkerClickListener(this);
        googleMap.setOnInfoWindowClickListener(this);
    }
    @Override
    public void onInfoWindowClick(Marker marker) {

        String userId = (String) marker.getTag();
        Log.i(TAG," User clicked is "+userId);
        // Check if a click count was set, then display the click count.
        if (userId != null) {
            Intent userViewInt = new Intent(this, UserView.class);
            userViewInt.putExtra("swamiUserId",userId);
            startActivity(userViewInt);
        }
    }
    public void callBackPinUsers(String result){
        Log.i(TAG,"Result is "+result);
        googleMap.clear();
        if (result != null && result.trim().length() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserDTO>>() {}.getType();
            List<UserDTO> fromJson = gson.fromJson(result, type);
            Log.i(TAG,"Users "+fromJson);
            LatLng userLoc=null;
            for(UserDTO u: fromJson){
                Log.i(TAG,"Location lat  "+u.getLat()+" longi "+u.getLon());
                if(Util.isEmpty(u.getLon()) || Util.isEmpty(u.getLat())) continue;
                Double dlon=Double.parseDouble(u.getLon());
                Double dlat=Double.parseDouble(u.getLat());
                userLoc = new LatLng(dlon, dlat);
                Marker marker= googleMap.addMarker(new MarkerOptions().position(userLoc)
                        .title(u.getFirstName()+","+u.getLastName()).icon(BitmapDescriptorFactory.fromResource(R.drawable.logo)));
                marker.setTag(u.getUserId());
                if(u.getCity()!=null && u.getCity().trim().length()>0)
                    marker.setSnippet("City "+u.getCity());
                marker.showInfoWindow();

            }
            if(userLoc!=null) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(userLoc));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(10.0f));
            }

        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // Retrieve the data from the marker.
        String userId = (String) marker.getTag();
        Log.i(TAG," User clicked is "+userId);
        // Check if a click count was set, then display the click count.
        if (userId != null) {

            Toast.makeText(this,
                    marker.getTitle() +
                            " has been clicked " + userId + " times.",
                    Toast.LENGTH_SHORT).show();
            Intent userViewInt = new Intent(this, UserView.class);
            userViewInt.putExtra("swamiUserId",userId);
            startActivity(userViewInt);
        }
        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.action_settings:
                Intent home=new Intent(MapsMarkerActivity.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
