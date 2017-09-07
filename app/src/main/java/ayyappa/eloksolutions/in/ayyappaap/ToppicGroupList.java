package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;

/**
 * Created by welcome on 9/6/2017.
 */

public class ToppicGroupList extends AppCompatActivity {

    Context context;
    AmazonS3 s3;
    TransferUtility transferUtility;

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

        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"user/groups/"+userId;
        System.out.println("userId :: "+userId+"firstName :: "+firstName+"lastName :: "+lastName);
        GetTopicGroups getGroups=new GetTopicGroups(context, url,rvGroups,s3,transferUtility);
        System.out.println("url for group list"+url+userId);
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
}



