package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;

import java.io.File;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.DiscussionDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.DiscussionHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.UserViewHelper;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;


/**
 * Created by welcome on 6/30/2017.
 */

public class UserView extends CardViewActivity {
    ImageView userImage,discussionCreate;
    TextView userName, userLocation;
    String userId;
    private BottomBar bottomBar;
    Context context;
    int count;
    File fileToDownload ;
    AmazonS3 s3;
    RegisterDTO registerDTO;
    TransferUtility transferUtility;
    TransferObserver transferObserver;
    Glide glide;
    TextView contacts;
    String tag="TopicView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivities_listview);
        userName=(TextView) findViewById(R.id.user_name);
        userLocation=(TextView) findViewById(R.id.user_location);
        contacts=(TextView) findViewById(R.id.user_contacts);
        TextView groups=(TextView) findViewById(R.id.user_groups);
        context=this;
        userId=getIntent().getStringExtra("userId");
        Log.i(tag, "userId is getStringExtra(\"userId\")"+userId);
        final Context ctx = this;
        try {

            File outdirectory=this.getCacheDir();
            fileToDownload=File.createTempFile("GRO","jpg",outdirectory);

        }catch (Exception e){
            e.printStackTrace();
        }
        credentialsProvider();
        setTransferUtility();

        UserViewHelper gettopicValue=new UserViewHelper(this);
        String surl = Config.SERVER_URL+"user/user/"+userId;
        System.out.println("url for group topic view list"+surl);
        try {
            String output=gettopicValue.new UserViewTask(surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);
            setFileToDownload("groups/G_302_1505918747142");
        }catch (Exception e){}

        FloatingActionButton userUpDate = (FloatingActionButton) findViewById(R.id.fabuser);
        userUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topicUp = new Intent(ctx, UserUpdate.class);
                topicUp.putExtra("userId",""+userId);
                startActivity(topicUp);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home1:
                        Intent intent1 = new Intent(UserView.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:

                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(UserView.this, PadiPoojaFull.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(UserView.this, MapsActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(UserView.this, OwnerView.class);
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
    public void transferObserverListener(TransferObserver transferObserver){

        //Bitmap bit= ImageUtils.getInstant().getCompressedBitmap(fileToDownload.getAbsolutePath());
        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("File down load status ", state+"");
                Log.i("File down load id", id+"");
                if("COMPLETED".equals(state.toString())){
                    //  Bitmap bit= BitmapFactory.decodeFile(fileToDownload.getAbsolutePath());
                    //  padiImage.setImageBitmap(bit);
                    glide.with(context).load(fileToDownload.getAbsolutePath()).into(userImage);

                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }


        });
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
    public void setFileToDownload(String imageKey){
        if (Util.isEmpty(imageKey))return;

        transferObserver = transferUtility.download(
                "elokayyappa",     // The bucket to download from *//*
                imageKey,    // The key for the object to download *//*
                fileToDownload        // The file to download the object to *//*
        );

        transferObserverListener(transferObserver);

    }
    public void userContacts(View view){
        Intent topicUp = new Intent(this, UserContactList.class);
        topicUp.putExtra("userId",""+userId);
        startActivity(topicUp);


    }
    public void userGroups(View view){
        Intent topicUp = new Intent(this, UserContactList.class);
        topicUp.putExtra("userId",""+userId);
        startActivity(topicUp);


    }
    public void userPadipooja(View view){
        Intent topicUp = new Intent(this, UserContactList.class);
        topicUp.putExtra("userId",""+userId);
        startActivity(topicUp);

    }
    public void userInvite(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, registerDTO.getFirstName()+" is invite to ayyappaApp");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
    public void userFeedBack(View view) {
        Intent userFeed = new Intent(this, FeedBackForm.class);
        userFeed.putExtra("userId",""+userId);
        startActivity(userFeed);
    }
    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from User Results" + result);
        if (result != null) {
            Gson gson = new Gson();
             registerDTO = gson.fromJson(result, RegisterDTO.class);
            userName.setText(registerDTO.getFirstName() + "  " + registerDTO.getLastName());
            userLocation.setText(registerDTO.getCity() + ", " + registerDTO.getArea());


        }
    }
    public void  setValuesTogroupTextFields(String result) {
        System.out.println("json xxxx from User Results" + result);
        if (result!=null){
            Gson gson = new Gson();
            RegisterDTO fromJsonn = gson.fromJson(result, RegisterDTO.class);



            }
        }

    private String saveEventToServer() {
        DiscussionDTO discussionDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(UserView.this)) {
                DiscussionHelper createtopicpHelper = new DiscussionHelper(UserView.this);
                String gurl = Config.SERVER_URL +"topic/addDiscussion";
                try {
                    String gId= createtopicpHelper.new CreateDiscussion(discussionDto, gurl).execute().get();
                    return gId;


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(UserView.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private DiscussionDTO buildDTOObject() {
        DiscussionDTO discussionDTO= new DiscussionDTO();
        String gname= userName.getText().toString();
        discussionDTO.setComment(gname);
        discussionDTO.setTopicId(userId);
        discussionDTO.setOwnerId(Config.userId);
        return discussionDTO;
    }



    private boolean checkValidation() {
        boolean ret = true;

        return ret;
    }
    }

