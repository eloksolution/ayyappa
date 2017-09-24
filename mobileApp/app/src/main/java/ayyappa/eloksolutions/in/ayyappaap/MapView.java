package ayyappa.eloksolutions.in.ayyappaap;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;



/**
 * Created by Krishna Kishore on 31-03-2017.
 */

public class MapView extends AppCompatActivity implements LocationListener,OnMapReadyCallback
{
    double latitude;
    double longitude;
    LatLng myPin;
    DrawerLayout drawerLayout;
    ImageView menu,pin_loc,neighbours;
    ListView neighbourList;
    com.google.android.gms.maps.MapView mMapView;
    private GoogleMap googleMap;
    CheckBox list_map_checkbox;
    boolean loadingMore = false;
    private HashSet<Integer> pages=new HashSet<>(10);
    int current_page=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        setContentView(R.layout.activity_maps);




        latitude = getIntent().getDoubleExtra("latitude", 0.0d);
        longitude = getIntent().getDoubleExtra("longitude", 0.0d);
        final String title = getIntent().getStringExtra("title");
        System.out.println("latitude " + latitude);
        System.out.println("longitude " + longitude);
        myPin = new LatLng(latitude, longitude);

        myPin = new LatLng(latitude, longitude);

        mMapView = (com.google.android.gms.maps.MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume(); // needed to get the map to display immediately
        try {
            MapsInitializer.initialize(this.getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                LatLng pin=new LatLng(0,0);
                int height = 100;
                int width = 100;
                BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.locate);
                Bitmap b = bitmapdraw.getBitmap();
                Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

                pin = new LatLng(latitude,longitude);
                Marker marker= googleMap.addMarker(new MarkerOptions().position(pin).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).title(title));
                // For zooming automatically to the location of the marker
                marker.showInfoWindow();
                CameraPosition cameraPosition = new CameraPosition.Builder().target(pin).zoom(14.5f).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });


    }
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                //startActivity(new Intent(getBaseContext(), .class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
