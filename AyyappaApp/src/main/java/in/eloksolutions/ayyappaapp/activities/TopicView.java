package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.DiscussionDTO;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;
import in.eloksolutions.ayyappaapp.beans.TopicDTO;
import in.eloksolutions.ayyappaapp.beans.TopicDissDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.DiscussionHelper;
import in.eloksolutions.ayyappaapp.helper.TopicViewHelper;
import in.eloksolutions.ayyappaapp.recycleviews.CheckInternet;
import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerDisscusion;
import in.eloksolutions.ayyappaapp.util.DisObject;


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
    Button playButton;
    Toolbar toolbar;
    String tag="TopicView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_view);
        user_name=(TextView) findViewById(R.id.user_name);
        date=(TextView) findViewById(R.id.date);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Topic View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        topicName=(TextView) findViewById(R.id.topic_view_title);
        description=(TextView) findViewById(R.id.forum_desc);
        discussionCreate =(ImageView) findViewById(R.id.send_button);
        addDisscussion =(EditText) findViewById(R.id.topic_text);
        topicImage=(ImageView) findViewById(R.id.forum_image);
        playButton=(Button) findViewById(R.id.play);
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
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String video_url=topicDTO.getDescription();
                String video_code = (video_url.substring(video_url.lastIndexOf("=") + 1));
                Intent topicView=new Intent(view.getContext(), PlayYoutubeActivity.class);
                topicView.putExtra("uri",video_code);

                view.getContext().startActivity(topicView);
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

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from Topic" + result);
        if (result!=null){
            Gson gson = new Gson();
             topicDTO = gson.fromJson(result, TopicDTO.class);

            user_name.setText(topicDTO.getOwnerName());
            toolbar.setTitle(topicDTO.getTopic());
            System.out.println("json xxxx from Topic" + topicDTO.getTopic());
            topicName.setText(topicDTO.getTopic());

            if(topicDTO.getDescription().contains("youtube")){
                playButton.setVisibility(View.VISIBLE);
            }else{
                description.setVisibility(View.VISIBLE);
                description.setText(topicDTO.getDescription());

            }

            if(topicDTO.getImgPath()!=null) {
                glide.with(context).load(Config.S3_URL + topicDTO.getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(topicImage);
            }else{
                glide.with(context).load(R.drawable.defaulta).diskCacheStrategy(DiskCacheStrategy.ALL).into(topicImage);
            }
       //     System.out.println("object resul myrecycler results list view is " + topicDTO.getDiscussions());
            if (topicDTO.getDiscussions()!=null) {
                ArrayList results = new ArrayList<DisObject>();
                for (TopicDissDTO d : topicDTO.getDiscussions()) {
                    DisObject disObject=new DisObject(d.getUserId(),d.getUserName(),d.getsPostDate(),d.getDissId(),d.getComment(),R.drawable.defaulta);
                    results.add(disObject);

                }
                try {

                    MyRecyclerDisscusion mAdapter = new MyRecyclerDisscusion(results, context);
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
                Intent home=new Intent(TopicView.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

