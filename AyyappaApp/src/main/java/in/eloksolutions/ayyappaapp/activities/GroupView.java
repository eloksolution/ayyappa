package in.eloksolutions.ayyappaapp.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.GroupDTO;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;
import in.eloksolutions.ayyappaapp.beans.TopicDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GetTopics;
import in.eloksolutions.ayyappaapp.helper.GroupViewHelper;
import in.eloksolutions.ayyappaapp.helper.TopicHelper;
import in.eloksolutions.ayyappaapp.recycleviews.CheckInternet;
import in.eloksolutions.ayyappaapp.recycleviews.Validation;


/**
 * Created by welcome on 6/30/2017.
 */

public class GroupView extends AppCompatActivity {
    ImageView groupImage,topicCrate;
    TextView groupName, description, noOfJoins,likescount,groupUpdate,groupShare,findMore;
    EditText addTopic;
    Button post;
    RecyclerView groupTopics;
    private boolean clicked = false;
    ImageView groupJoin,like;
    String groupId, userId,firstName,lastName;
    int likescounts;
    FloatingActionButton fab;
    public static int joinedStatus;
    boolean attimage=false;
    Button joinButton;
    Toolbar toolbar;
    Glide glide;
    CollapsingToolbarLayout collapsingToolbar;
    Context context;
    public static String joinStatus="Y";
    int count;
    ImageView image;
    String tag="GroupView";
    GroupDTO groupDTO;
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
        Log.i(tag, "preferences.getString userId is"+userId+","+firstName+""+lastName);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Group View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        groupName = (TextView) findViewById(R.id.group_view_title);
      //  groupJoin =(ImageView) findViewById(R.id.group_join);
        description = (TextView) findViewById(R.id.group_view_desc);
        addTopic = (EditText) findViewById(R.id.add_topic);
         topicCrate =(ImageView) findViewById(R.id.but_topic);
       groupImage =(ImageView) findViewById(R.id.group_image_view);
        groupUpdate=(TextView) findViewById(R.id.group_update);
        noOfJoins =(TextView) findViewById(R.id.joinedcount);
        joinButton=(Button) findViewById(R.id.group_join);
        groupShare=(TextView) findViewById(R.id.share_text);
        findMore=(TextView) findViewById(R.id.findmore);
         fab = (FloatingActionButton) findViewById(R.id.fabgroup);
        post=(Button) findViewById(R.id.create_topic);
        CoordinatorLayout group_view_layout = (CoordinatorLayout) findViewById(R.id.group_view_layout);
        LinearLayout event_layout=(LinearLayout) findViewById(R.id.event_layout);
        LinearLayout share_layout=(LinearLayout) findViewById(R.id.share_layout);
        LinearLayout join_layout=(LinearLayout) findViewById(R.id.joined_layout);

        final Context ctx = this;
/*        topicCrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String createGroupHelper=saveEventToServer();
                Intent groupView = new Intent(ctx, GroupView.class);
                groupView.putExtra("groupId", ""+groupId);
                startActivity(groupView);
            }
        }); */
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topicCreate=new Intent(GroupView.this,CreateGroupTopic.class);
                topicCreate.putExtra("groupId", groupId);
                startActivity(topicCreate);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topicCreate=new Intent(GroupView.this,CreateGroupTopic.class);
                topicCreate.putExtra("groupId", groupId);
                startActivity(topicCreate);
            }
        });


        join_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent groupMembers=new Intent(view.getContext(), GroupMembersList.class);
                groupMembers.putExtra("groupId", ""+groupId);
                view.getContext().startActivity(groupMembers);

            }
        });

        findMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(clicked) {
                    findMore.setText("Find More ");
                    description.setMaxLines(2);
                    clicked = false;
                } else {
                    findMore.setText("Show Less ");
                    description.setMaxLines(Integer.MAX_VALUE);
                    clicked = true;
                }
            }
        });


        share_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent= new Intent();
                    String sAux ="\n"+groupName+" @Ayyappa\n for more Groups \n";
                    //String title= groupName.replaceAll(" ","_")+"@MELZOL";
                    String msg=sAux+"https://wdq3a.app.goo.gl/?link=https://melzol.in/1/"+groupId+"&apn=in.melzol" +
                            "&st="+groupName+"&si=";
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_TEXT,msg);
                    intent.setType("text/plain");
                    startActivity(Intent.createChooser(intent,"Share this Group"));
                } catch(Exception e) {
                    e.toString();
                }

            }
        });



        event_layout.setOnClickListener(new View.OnClickListener() {
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
        String surl = Config.SERVER_URL+"group/getgroup/"+groupId+"/"+userId;
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new GroupViewTask(surl).execute().get();
            System.out.println("the output from Group"+output);
           // setValuesToTextFields(output);

        }catch (Exception e){
            e.printStackTrace();
        }

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                joinEvent();
                joinButton.setVisibility(View.GONE);
            }
        });
    }

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from groupview" + result);
        if (result!=null){
            Gson gson = new Gson();
             groupDTO = gson.fromJson(result, GroupDTO.class);
            groupName.setText(groupDTO.getName());
            description.setText(groupDTO.getDescription());
            toolbar.setTitle(groupDTO.getName());
            glide.with(context).load(Config.S3_URL+groupDTO.getImagePath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(groupImage);
            if(groupDTO.getGroupMembers()!=null) {
                noOfJoins.setText(groupDTO.getGroupMembers().size() + "");
                System.out.println("json xxxx from groupDTO.getGroupMembers().size()" + groupDTO.getGroupMembers().size());
            }
            try {
                if(userId.equals(groupDTO.getOwner()) || joinStatus.equals(groupDTO.getIsMember()) ){
                    System.out.println("groupDTO.getIsMember()"+groupDTO.getIsMember());
                    joinButton.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
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
        Log.i(tag,"group joined time"+groupMembers);
        return groupMembers;

    }
    private void joinEvent() {
        GroupViewHelper groupJoinHelper = new GroupViewHelper(this);
        GroupMembers groupJoins = memBuildDTOObject();
        String surl = Config.SERVER_URL + "group/join";
        try {
            String joinmem=groupJoinHelper.new JoinGroup(groupJoins,surl).execute().get();
            System.out.println("the output from JoinEvent"+joinmem);
            FirebaseMessaging.getInstance().subscribeToTopic(groupJoins.getGroupId());
            addingMember(joinmem);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void createTopicDialog() {

        final Dialog dialog = new Dialog(GroupView.this);
        // Include dialog.xml file
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.topic_create);
        Window window = dialog.getWindow();
        // window.setGravity(Gravity.BOTTOM | Gravity.LEFT |Gravity.END);
        WindowManager.LayoutParams param = window.getAttributes();
        param.gravity = Gravity.FILL;
        param.width = WindowManager.LayoutParams.MATCH_PARENT;
        param.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.setCanceledOnTouchOutside(true);

        image = (ImageView) dialog.findViewById(R.id.forum_image);
        final EditText links = (EditText) dialog.findViewById(R.id.post_text);
        TextView post = (TextView) dialog.findViewById(R.id.post);
        TextView topic_text = (TextView) dialog.findViewById(R.id.topic_text);
        LinearLayout photo_card = (LinearLayout) dialog.findViewById(R.id.photo_card);

        photo_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attimage = true;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

            }
        });
    }


    private void addingMember(String result) throws JSONException {
        JSONObject jsonObject;
        jsonObject = new JSONObject(result);
        groupJoin.setVisibility(View.GONE);
        count=count+1;
       //noOfJoins.setText(count + " members are going");

    }
    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(addTopic)) ret = false;
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
                Intent home=new Intent(GroupView.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    }
