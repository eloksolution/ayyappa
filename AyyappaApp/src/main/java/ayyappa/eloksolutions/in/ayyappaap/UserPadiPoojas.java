package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.roughike.bottombar.BottomBar;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetEvents;

/**
 * Created by welcome on 1/4/2017.
 */

public class UserPadiPoojas extends CardViewActivity {
    Context context;
    private BottomBar bottomBar;
    AmazonS3 s3;
    TransferUtility transferUtility;
    String UserId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.padipoojafull);
        context=this;

        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvPadi);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        UserId=preferences.getString("userId", null);
        String url= Config.SERVER_URL+"user/padis/"+UserId;
        GetEvents getEvents=new GetEvents(context,url,rvPadi,s3,transferUtility);
        getEvents.execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_padi);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupCreate= new Intent(UserPadiPoojas.this, CreatePadiPooja.class);
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
                        Intent intent1 = new Intent(UserPadiPoojas.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:
                        Intent intent2 = new Intent(UserPadiPoojas.this, GroupList.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_books:

                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(UserPadiPoojas.this, MapsActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(UserPadiPoojas.this, OwnerView.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });
    }
    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:22bb863b-3f88-4322-8cee-9595ce44fc48", // Identity Pool ID
                Regions.AP_NORTHEAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

    }

    public void setTransferUtility(){
        transferUtility = new TransferUtility(s3, getApplicationContext());
    }
}
