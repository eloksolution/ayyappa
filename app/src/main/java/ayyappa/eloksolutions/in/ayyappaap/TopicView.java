package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
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

import com.google.gson.Gson;

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


/**
 * Created by welcome on 6/30/2017.
 */

public class TopicView extends AppCompatActivity {
    ImageView TopicImage,discussionCreate;
    TextView topicName, description, noOfJoins;
    EditText addDisscussion;
    RecyclerView rvPadi;
    Button topicUpDate;

    String topicId;

    Context context;
    int count;
    String tag="TopicView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_view);
        discussionCreate =(ImageView) findViewById(R.id.but_diss);
        topicName=(TextView) findViewById(R.id.topic_view_title);
        description=(TextView) findViewById(R.id.topic_view_desc);
        addDisscussion =(EditText) findViewById(R.id.add_discu);
        topicUpDate =(Button) findViewById(R.id.but_topic_update);
        context=this;
        topicId=getIntent().getStringExtra("topicId");
        Log.i(tag, "topicId is"+topicId);
        final Context ctx = this;

        TopicViewHelper gettopicValue=new TopicViewHelper(this);
        String surl = Config.SERVER_URL+"topic/"+topicId;
        System.out.println("url for group topic view list"+surl);
        try {
            String output=gettopicValue.new TopicViewTask(surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}
        topicUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topicUp = new Intent(ctx, TopicUpdate.class);
                topicUp.putExtra("topicId",""+topicId);
                startActivity(topicUp);
            }
        });

        discussionCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String createdisscussionHelper=saveEventToServer();
                Intent topicView = new Intent(ctx, TopicView.class);
                topicView.putExtra("id",createdisscussionHelper);
                startActivity(topicView);
            }
        });
        rvPadi = (RecyclerView) findViewById(R.id.rv_disscussions);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);

    }

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from Topic" + result);
        if (result!=null){
            Gson gson = new Gson();
            TopicDTO fromJsonn = gson.fromJson(result, TopicDTO.class);
            topicName.setText(fromJsonn.getTopic());
            description.setText(fromJsonn.getDescription());

            System.out.println("object resul myrecycler results list view is " + fromJsonn.getDiscussions());
            if (fromJsonn.getDiscussions()!=null) {
                ArrayList results = new ArrayList<DisObject>();
                for (TopicDissDTO d : fromJsonn.getDiscussions()) {
                    DisObject disObject=new DisObject(d.getUserId(),d.getsPostDate(),d.getDissId(),d.getComment(),R.drawable.ayyappa_logo);
                    results.add(disObject);

                }
                MyRecyclerDisscusion mAdapter = new MyRecyclerDisscusion(results);
                rvPadi.setAdapter(mAdapter);
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
        discussionDTO.setOwnerId(Config.User_ID);
        return discussionDTO;
    }
    private GroupMembers memBuildDTOObject() {
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroupId(topicId);
        groupMembers.setUserId("595dfb76b3708f62b1f794ae");
        groupMembers.setFirstname("suresh");
        groupMembers.setLastName("ramesh");
        return groupMembers;
    }


    private boolean checkValidation() {
        boolean ret = true;


        return ret;
    }
    }

