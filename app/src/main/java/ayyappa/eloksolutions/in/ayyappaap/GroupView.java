package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.GroupDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.GroupMembers;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetTopics;
import ayyappa.eloksolutions.in.ayyappaap.helper.GroupViewHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.TopicHelper;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;


/**
 * Created by welcome on 6/30/2017.
 */

public class GroupView extends AppCompatActivity {
    ImageView groupImage,topicCrate;
    TextView groupName, description, noOfJoins,likescount;
    EditText addTopic;
    RecyclerView groupTopics;
    ImageView groupJoin, groupShare, groupUpdate,like;
    String groupId, userId,firstName,lastName;
    int likescounts;
    public static int joinedStatus;
    File fileToDownload ;
    AmazonS3 s3;
    Button joinButton;
    TransferUtility transferUtility;
    Context context;
    int count;
    String tag="GroupView";
    GroupDTO groupDTO;
    TransferObserver transferObserver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);
        context=this;
         groupId=getIntent().getStringExtra("groupId");
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
       userId= preferences.getString("userId",null);
        firstName=preferences.getString("firstName",null);
        lastName=preferences.getString("lastName",null);
        Log.i(tag, "groupId is"+groupId);
        Log.i(tag, "preferences.getString userId is"+groupId+","+firstName+""+lastName);
        groupName = (TextView) findViewById(R.id.group_view_title);
        groupJoin =(ImageView) findViewById(R.id.group_join);
        description = (TextView) findViewById(R.id.group_view_desc);
        addTopic = (EditText) findViewById(R.id.add_topic);
         topicCrate =(ImageView) findViewById(R.id.but_topic);
       groupImage =(ImageView) findViewById(R.id.group_image_view);
        groupUpdate=(ImageView) findViewById(R.id.group_update);
        noOfJoins =(TextView) findViewById(R.id.joinedcount);
        like=(ImageView) findViewById(R.id.group_likes);
        likescount=(TextView) findViewById(R.id.like_count);
        joinButton=(Button) findViewById(R.id.joinbtn);
        groupShare=(ImageView) findViewById(R.id.group_share);


        try {

        File outdirectory=this.getCacheDir();
        fileToDownload=File.createTempFile("GRO","jpg",outdirectory);

        }catch (Exception e){
            e.printStackTrace();
        }
        credentialsProvider();


        // callback method to call the setTransferUtility method
        setTransferUtility();
        final Context ctx = this;
        topicCrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String createGroupHelper=saveEventToServer();
                Intent groupView = new Intent(ctx, GroupView.class);
                groupView.putExtra("groupId", ""+groupId);
                startActivity(groupView);
            }
        });

        groupJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent groupMembers=new Intent(view.getContext(), GroupMembersList.class);
                groupMembers.putExtra("groupId", ""+groupId);
                view.getContext().startActivity(groupMembers);

            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

     joinButton.setVisibility(View.GONE);
     joinEvent();

            }
        });

        groupShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent= new Intent();
                    String sAux ="\n"+groupName+" @MELZOL\n for more Groups \n";
                    //String title= groupName.replaceAll(" ","_")+"@MELZOL";
                    String msg=sAux+"https://wdq3a.app.goo.gl/?link=https://melzol.in/1/"+groupId+"&apn=in.melzol" +
                            "&st="+groupName+"&si=";
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,msg);
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent,"Share this Group"));
                } catch(Exception e) {
                    //e.toString();
                }

            }
        });

        groupUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupUdate=new Intent(view.getContext(), GroupUpdate.class);
                groupUdate.putExtra("groupId", ""+groupId);
                view.getContext().startActivity(groupUdate);

            }
        });
        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rv_topics);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"topic/getGroupTopics/"+groupId;
        GetTopics getTopics=new GetTopics(context,url,rvPadi);
        getTopics.execute();


        GroupViewHelper getGroupsValue=new GroupViewHelper(this);
        String surl = Config.SERVER_URL+"group/getgroup/"+groupId+"/598839b6e4b0ca1af7a13b";
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new GroupViewTask(surl).execute().get();
            System.out.println("the output from Group"+output);
            setValuesToTextFields(output);
            System.out.println("groupDTO.getImagePath()"+groupDTO.getImagePath());
            setFileToDownload(groupDTO.getImagePath());


        }catch (Exception e){
            e.printStackTrace();
        }


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

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("File down load status", state+"");
                Log.i("File down load id", id+"");
                if("COMPLETED".equals(state.toString())){
                    Bitmap bit= BitmapFactory.decodeFile(fileToDownload.getAbsolutePath());
                    groupImage.setImageBitmap(bit);
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

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from groupview" + result);
        if (result!=null){
            Gson gson = new Gson();
             groupDTO = gson.fromJson(result, GroupDTO.class);
            groupName.setText(groupDTO.getName());
            description.setText(groupDTO.getDescription());
            if(groupDTO.getGroupMembers()!=null) {
                noOfJoins.setText(groupDTO.getGroupMembers().size() + "  joins");
                System.out.println("json xxxx from groupDTO.getGroupMembers().size()" + groupDTO.getGroupMembers().size());

            }

        }
    }
    private String saveEventToServer() {
        TopicDTO topicDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(GroupView.this)) {
                TopicHelper createtopicpHelper = new TopicHelper(GroupView.this);
                String gurl = Config.SERVER_URL +"topic/add";
                try {
                    String gId= createtopicpHelper.new CreateTopic(topicDto, gurl).execute().get();
                    return gId;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(GroupView.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private TopicDTO buildDTOObject() {
        TopicDTO topicDto= new TopicDTO();
        String gname= addTopic.getText().toString();
        topicDto.setTopic(gname);
        String gdesc= description.getText().toString();
        topicDto.setDescription(gdesc);
       topicDto.setGroupId(groupId);

        topicDto.setOwner(firstName);
        return topicDto;
    }
    private GroupMembers memBuildDTOObject() {
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroupId(groupId);
        groupMembers.setUserId(userId);
        groupMembers.setFirstname(firstName);
        groupMembers.setLastName(lastName);
        return groupMembers;
    }
    private void joinEvent() {
        GroupViewHelper groupJoinHelper = new GroupViewHelper(this);
        GroupMembers groupJoins = memBuildDTOObject();

        String surl = Config.SERVER_URL + "group/join";
        try {
            String joinmem=groupJoinHelper.new JoinGroup(groupJoins,surl).execute().get();
            System.out.println("the output from JoinEvent"+joinmem);
            addingMember(joinmem);
        }catch (Exception e){}
    }

    private void addingMember(String result) throws JSONException {
        JSONObject jsonObject;
        jsonObject = new JSONObject(result);

        groupJoin.setVisibility(View.GONE);

        count=count+1;
       // noOfJoins.setText(count + " members are going");


    }
    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(addTopic)) ret = false;
        return ret;
    }
    }

