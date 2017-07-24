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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.GroupDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.GroupMembers;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetTopics;
import ayyappa.eloksolutions.in.ayyappaap.helper.GroupViewHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.TopicHelper;


/**
 * Created by welcome on 6/30/2017.
 */

public class GroupView extends AppCompatActivity {
    ImageView groupImage,topicCrate;
    TextView groupName, description, noOfJoins;
    EditText addTopic;
    RecyclerView groupTopics;
    Button groupJoin, groupLike,groupShare, groupUpdate;
    String groupId;

    Context context;
    int count;
    String tag="GroupView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_view);
        context=this;
         groupId=getIntent().getStringExtra("groupId");
        Log.i(tag, "groupId is"+groupId);
        groupName = (TextView) findViewById(R.id.group_view_title);
        groupJoin =(Button) findViewById(R.id.group_join);
        description = (TextView) findViewById(R.id.group_view_desc);
        addTopic = (EditText) findViewById(R.id.add_topic);
         topicCrate =(ImageView) findViewById(R.id.but_topic);
        noOfJoins =(TextView) findViewById(R.id.group_join_count);
        groupUpdate=(Button) findViewById(R.id.group_update);

        final Context ctx = this;
        topicCrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String createGroupHelper=saveEventToServer();
                Intent groupView = new Intent(ctx, GroupView.class);
                groupView.putExtra("id",createGroupHelper);
                startActivity(groupView);
            }
        });
        groupJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                groupJoin.setVisibility(View.GONE);
                joinEvent();

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
        String url= Config.SERVER_URL+"topic/getTopics";
        GetTopics getTopics=new GetTopics(context,url,rvPadi);
        getTopics.execute();


        GroupViewHelper getGroupsValue=new GroupViewHelper(this);
        String surl = Config.SERVER_URL+"group/groupEdit/"+groupId;
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new GroupViewTask(surl).execute().get();
            System.out.println("the output from Group"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}


    }

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from groupview" + result);
        if (result!=null){
            Gson gson = new Gson();
            GroupDTO fromJsonn = gson.fromJson(result, GroupDTO.class);
            groupName.setText(fromJsonn.getName());
            description.setText(fromJsonn.getDescription());



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
        topicDto.setOwner("suresh");
        return topicDto;
    }
    private GroupMembers memBuildDTOObject() {
        GroupMembers groupMembers = new GroupMembers();
        groupMembers.setGroupId(groupId);
        groupMembers.setUserId("595dfb76b3708f62b1f794ae");
        groupMembers.setFirstname("suresh");
        groupMembers.setLastName("ramesh");
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
        noOfJoins.setText(count + " members are going");


    }
    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(addTopic)) ret = false;


        return ret;
    }
    }

