package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.roughike.bottombar.BottomBar;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GetSwamiRequests;


public class SwamiRequest extends AppCompatActivity {
    Context context;
    AmazonS3 s3;
   TransferUtility transferUtility;
    String userId;
    private BottomBar bottomBar;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Swami Request List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
      FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabgroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent groupCreate= new Intent(SwamiRequest.this, CreateGroup.class);
                startActivity(groupCreate);
            }
        });
        TextView noData=(TextView) findViewById(R.id.tv_no_data);
        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES,MODE_PRIVATE);
        userId=preferences.getString("userId",null);
        String url= Config.SERVER_URL+"user/receivedConnections/"+userId;
        GetSwamiRequests getGroups=new GetSwamiRequests(SwamiRequest.this,url,rvGroups,noData);
        System.out.println("url for group list"+url);
        getGroups.execute();

       /* BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home1:
                        Intent intent1 = new Intent(SwamiRequest.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:

                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(SwamiRequest.this, PadiPoojaFull.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(SwamiRequest.this, MapsMarkerActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(SwamiRequest.this, OwnerView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });*/

    }
    public void contacList( String userId) {
       Intent groupView = new Intent(SwamiRequest.this, UserView.class);
        groupView.putExtra("swamiUSerId", userId);
        startActivity(groupView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.action_settings:
                Intent home=new Intent(SwamiRequest.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}


