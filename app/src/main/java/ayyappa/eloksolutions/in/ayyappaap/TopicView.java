package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.GroupMembers;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.TopicHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.TopicViewHelper;


/**
 * Created by welcome on 6/30/2017.
 */

public class TopicView extends AppCompatActivity {
    ImageView TopicImage,discussionCrate;
    TextView topicName, description, noOfJoins;
    EditText addDisscussion;

    String topicId;

    Context context;
    int count;
    String tag="TopicView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_view);
        context=this;
        topicId=getIntent().getStringExtra("topicId");
        Log.i(tag, "topicId is"+topicId);
        final Context ctx = this;
        discussionCrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String createdisscussionHelper=saveEventToServer();
                Intent topicView = new Intent(ctx, TopicView.class);
                topicView.putExtra("id",createdisscussionHelper);
                startActivity(topicView);
            }
        });




        TopicViewHelper getGroupsValue=new TopicViewHelper(this);
        String surl = Config.SERVER_URL+"topic/"+topicId;
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new TopicViewTask(surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}


    }

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from groupview" + result);
        if (result!=null){
            Gson gson = new Gson();
            TopicDTO fromJsonn = gson.fromJson(result, TopicDTO.class);
            topicName.setText(fromJsonn.getTopic());
            description.setText(fromJsonn.getDescription());



        }
    }
    private String saveEventToServer() {
        TopicDTO topicDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(TopicView.this)) {
                TopicHelper createtopicpHelper = new TopicHelper(TopicView.this);
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
                CheckInternet.showAlertDialog(TopicView.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private TopicDTO buildDTOObject() {
        TopicDTO topicDto= new TopicDTO();
        String gname= addDisscussion.getText().toString();
        topicDto.setTopic(gname);
        String gdesc= description.getText().toString();
        topicDto.setDescription(gdesc);
        topicDto.setGroupId(topicId);
        topicDto.setOwner("suresh");
        return topicDto;
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

