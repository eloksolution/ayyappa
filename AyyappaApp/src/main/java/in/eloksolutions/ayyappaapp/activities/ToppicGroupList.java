package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GetTopicGroups;
import in.eloksolutions.ayyappaapp.util.Util;

/**
 * Created by welcome on 9/6/2017.
 */

public class ToppicGroupList extends AppCompatActivity {

    Context mcontext;
    AmazonS3 s3;
    TransferUtility transferUtility;
    Toolbar toolbar;
    String topicDesc,topicTitle,topicImage,userId,firstName,lastName;
    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.group_list);

        topicImage = getIntent().getStringExtra("topicImage");
        topicDesc= getIntent().getStringExtra("topicName");
        topicTitle = getIntent().getStringExtra("topicName");
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        userId=preferences.getString("userId",null);
        firstName=preferences.getString("firstName",null);
        lastName=preferences.getString("lastName",null);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Ask a Questions");
        toolbar.setTitle("Ask a Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        String surl= Config.SERVER_URL+"user/groups/59ad6e65e4b0f45b68f60617";
        System.out.println("userId :: "+userId+"firstName :: "+firstName+"lastName :: "+lastName);
        GetTopicGroups getGroups=new GetTopicGroups(mcontext, surl,rvGroups,s3,transferUtility);
        System.out.println("url for group list"+surl);
        getGroups.execute();

        credentialsProvider();
        setTransferUtility();
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
            case R.id.feed:
                Intent feed=new Intent(ToppicGroupList.this, FeedBackForm.class);
                startActivity(feed);
                return true;
            case R.id.share:
                startActivity(Util.getInviteIntent(firstName+" "+lastName));
                return true;
            case R.id.action_settings:
                Intent home=new Intent(ToppicGroupList.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}



