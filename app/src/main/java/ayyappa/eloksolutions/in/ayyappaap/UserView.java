package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.DiscussionDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.DiscussionHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.UserViewHelper;


/**
 * Created by welcome on 6/30/2017.
 */

public class UserView extends CardViewActivity {
    ImageView userImage,discussionCreate;
    TextView userName, userLocation;
    RegisterDTO registerDTO;
    String userId;
    private BottomBar bottomBar;
    Context context;
    int count;
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
        Log.i(tag, "userId is"+userId);
        final Context ctx = this;

        UserViewHelper gettopicValue=new UserViewHelper(this);
        String surl = Config.SERVER_URL+"user/user/"+userId;
        System.out.println("url for group topic view list"+surl);
        try {
            String output=gettopicValue.new UserViewTask(surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);
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

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_calls) {



                } else if (tabId == R.id.tab_groups) {

                    Intent i = new Intent(getBaseContext(), GroupList.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_chats) {
                    Intent i = new Intent(getBaseContext(), PadiPoojaFull.class);
                    startActivity(i);

                }
                else if (tabId == R.id.tab_home) {

                    Intent i = new Intent(getBaseContext(), MapsActivity.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_profile) {
                    Intent regiser=new Intent(getBaseContext(), UserView.class);
                    startActivity(regiser);


                }
            }
        });


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

