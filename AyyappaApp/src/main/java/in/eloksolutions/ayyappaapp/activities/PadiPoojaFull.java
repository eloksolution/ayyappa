package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.BottomNavigationViewHelper;
import in.eloksolutions.ayyappaapp.helper.GetEvents;
import in.eloksolutions.ayyappaapp.maps.MapsMarkerActivity;


/**
 * Created by welcome on 1/4/2017.
 */

public class PadiPoojaFull extends AppCompatActivity {
    Context context;
    private BottomBar bottomBar;
    String userId;
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.padipoojafull);
      Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       setSupportActionBar(toolbar);
       toolbar.setBackgroundColor(getResources().getColor(R.color.black));
       toolbar.setTitleTextColor(getResources().getColor(R.color.white));
       getSupportActionBar().setTitle("PadiPoojas List");
       getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
       TextView noData=(TextView) findViewById(R.id.tv_no_data);
       RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvPadi);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
       SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES,MODE_PRIVATE);
       userId=preferences.getString("userId",null);
        String url= Config.SERVER_URL+"padipooja/getpoojas/"+userId;
        GetEvents getEvents=new GetEvents(context,url,rvPadi, noData);
        getEvents.execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_padi);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupCreate= new Intent(PadiPoojaFull.this, CreatePadiPooja.class);
                startActivity(groupCreate);
            }
        });
       noData.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent groupCreate= new Intent(PadiPoojaFull.this, CreatePadiPooja.class);
               startActivity(groupCreate);
           }
       });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home1:
                        Intent intent1 = new Intent(PadiPoojaFull.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:
                        Intent intent2 = new Intent(PadiPoojaFull.this, GroupList.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_books:

                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(PadiPoojaFull.this, MapsMarkerActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(PadiPoojaFull.this, OwnerView.class);
                        startActivity(intent4);
                        break;
                }

                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
