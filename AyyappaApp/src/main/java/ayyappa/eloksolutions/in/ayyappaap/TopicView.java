package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.DiscussionDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.GroupMembers;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDissDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.DiscussionHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.TopicViewHelper;
import ayyappa.eloksolutions.in.ayyappaap.util.DisObject;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;


/**
 * Created by welcome on 6/30/2017.
 */

public class TopicView extends AppCompatActivity {
    ImageView topicImage,discussionCreate;
    TextView topicName, description,user_name,date;
    EditText addDisscussion;
    RecyclerView rvPadi;
    String topicId, usersId, firstName, lastName;
    Context context;
    File fileToDownload ;
    AmazonS3 s3;
    TopicDTO topicDTO;
    TransferUtility transferUtility;
    TransferObserver transferObserver;
    Glide glide;
    int count;
    String tag="TopicView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_view);
        user_name=(TextView) findViewById(R.id.user_name);
        date=(TextView) findViewById(R.id.date);
      //  topicName=(TextView) findViewById(R.id.topic_view_title);
        description=(TextView) findViewById(R.id.forum_desc);
        discussionCreate =(ImageView) findViewById(R.id.send_button);


        addDisscussion =(EditText) findViewById(R.id.topic_text);
        topicImage=(ImageView) findViewById(R.id.forum_image);

        context=this;
        topicId=getIntent().getStringExtra("topicId");
        Log.i(tag, "topicId is"+topicId);
        final Context ctx = this;
        try {

            File outdirectory=this.getCacheDir();
            fileToDownload=File.createTempFile("GRO","jpg",outdirectory);

        }catch (Exception e){
            e.printStackTrace();
        }
        credentialsProvider();
        setTransferUtility();

     /*   FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabtopic);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topicUp = new Intent(ctx, TopicUpdate.class);
                topicUp.putExtra("topicId",""+topicId);
                startActivity(topicUp);
            }
        }); */
        TopicViewHelper gettopicValue=new TopicViewHelper(this);
        String surl = Config.SERVER_URL+"topic/"+topicId;
        System.out.println("url for group topic view list"+surl);
        try {
            String output=gettopicValue.new TopicViewTask(surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);


        }catch (Exception e){
            e.printStackTrace();
        }


        discussionCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String createdisscussionHelper=saveEventToServer();
                Intent topicView = new Intent(ctx, TopicView.class);
                topicView.putExtra("topicId",""+topicId);
                startActivity(topicView);
            }
        });
        rvPadi = (RecyclerView) findViewById(R.id.rv_disscussions);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        usersId= preferences.getString("userId",null);
        firstName=preferences.getString("firstName",null);
        lastName=preferences.getString("lastName",null);
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

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from Topic" + result);
        if (result!=null){
            Gson gson = new Gson();
             topicDTO = gson.fromJson(result, TopicDTO.class);
            user_name.setText(topicDTO.getOwnerName());
         //   topicName.setText(fromJsonn.getTopic());
            description.setText(topicDTO.getDescription());
            glide.with(context).load(Config.S3_URL+topicDTO.getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(topicImage);


            System.out.println("object resul myrecycler results list view is " + topicDTO.getDiscussions());
            if (topicDTO.getDiscussions()!=null) {
                ArrayList results = new ArrayList<DisObject>();
                for (TopicDissDTO d : topicDTO.getDiscussions()) {
                    DisObject disObject=new DisObject(d.getUserId(),d.getUserName(),d.getsPostDate(),d.getDissId(),d.getComment(),R.drawable.ayyappa_logo);
                    results.add(disObject);

                }
                try {

                    MyRecyclerDisscusion mAdapter = new MyRecyclerDisscusion(results);


                    rvPadi.setAdapter(mAdapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("object result myrecycler results list view is " + results);

            }
        }
    }


    private String saveEventToServer() {
        DiscussionDTO discussionDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(TopicView.this)) {
                DiscussionHelper createtopicpHelper = new DiscussionHelper(TopicView.this);
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
                CheckInternet.showAlertDialog(TopicView.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private DiscussionDTO buildDTOObject() {
        DiscussionDTO discussionDTO= new DiscussionDTO();
        String gname= addDisscussion.getText().toString();
        discussionDTO.setComment(gname);
        discussionDTO.setTopicId(topicId);
        discussionDTO.setOwnerId(usersId);
        discussionDTO.setOwnerName(firstName+lastName);
        return discussionDTO;
    }
    private GroupMembers memBuildDTOObject() {
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroupId(topicId);
        groupMembers.setUserId(usersId);
        groupMembers.setFirstname(firstName);
        groupMembers.setLastName(lastName);
        return groupMembers;
    }


    private boolean checkValidation() {
        boolean ret = true;


        return ret;
    }
    }

