package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.roughike.bottombar.BottomBar;

import java.util.concurrent.ExecutionException;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.SendTagHelper;
import in.eloksolutions.ayyappaapp.helper.UserViewHelper;
import in.eloksolutions.ayyappaapp.recycleviews.CheckInternet;


/**
 * Created by welcome on 6/30/2017.
 */

public class UserView extends AppCompatActivity {
    ImageView userImage,discussionCreate;
    TextView userName, userLocation,sendRequest;
    String swamiUserId,fromFirstName,fromUserId,fromLastName;
    private BottomBar bottomBar;
    Context context;
    int count;
    AmazonS3 s3;
    RegisterDTO registerDTO;
    TransferUtility transferUtility;
    TransferObserver transferObserver;
    Glide glide;
    TextView contacts,sentText;
    ImageView tagRequest;
    String tag="TopicView";
    static  String requestSentValue="true";
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);
        userName=(TextView) findViewById(R.id.settings_username);
        userLocation=(TextView) findViewById(R.id.address_value);
        contacts=(TextView) findViewById(R.id.contacts_text);
        TextView groups=(TextView) findViewById(R.id.user_groups);
        userImage=(ImageView)findViewById(R.id.profile_img);
        sendRequest=(TextView) findViewById(R.id.tag_request_text);
        sentText=(TextView) findViewById(R.id.friend_text);
        context=this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User View");
        toolbar.setTitle("User View");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES,MODE_PRIVATE);
        fromUserId=preferences.getString("userId",null);
        fromFirstName=preferences.getString("firstName",null);
        fromLastName=preferences.getString("lastName",null);
        swamiUserId=getIntent().getStringExtra("swamiUserId");
        Log.i(tag, "userId is getStringExtra)"+swamiUserId);
        final Context ctx = this;
        UserViewHelper gettopicValue=new UserViewHelper(this);
        String surl = Config.SERVER_URL+"user/user/"+fromUserId+"/"+swamiUserId;
        System.out.println("url for group topic view list"+surl);
        try {
            String output=gettopicValue.new UserViewTask(this,surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);
           // setFileToDownload("groups/G_302_1505918747142");
        }catch (Exception e){}

       /* FloatingActionButton userUpDate = (FloatingActionButton) findViewById(R.id.fabuser);
        userUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topicUp = new Intent(ctx, UserUpdate.class);
                topicUp.putExtra("userId",""+swamiUserId);
                startActivity(topicUp);
            }
        });*/

        if (fromUserId.equals(swamiUserId)) {
            sendRequest.setVisibility(View.GONE);
        }

       /* BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
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
        }); */

    }

    public void SenTag(View view){
        userSendTag();
        Toast.makeText(this, "sended a request", Toast.LENGTH_LONG).show();
    }
    private String userSendTag() {

        RegisterDTO userDTo = buildDTOObject();

        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(UserView.this)) {
                SendTagHelper sendTag = new SendTagHelper(UserView.this);
                String gurl = Config.SERVER_URL +"user/requestConnection";
                try {
                    String gId= sendTag.new SenTagTask(userDTo, gurl).execute().get();
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

    public void userContacts(View view){
        Intent topicUp = new Intent(this, ContactActivity.class);
        topicUp.putExtra("userId",""+swamiUserId);
        startActivity(topicUp);


    }
    public void userGroups(View view){
        Intent topicUp = new Intent(this, UserGroups.class);
        topicUp.putExtra("userId",""+swamiUserId);
        startActivity(topicUp);


    }
    public void userPadipooja(View view){
        Intent topicUp = new Intent(this, UserPadiPoojas.class);
        topicUp.putExtra("userId",""+swamiUserId);
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
        userFeed.putExtra("userId",""+swamiUserId);
        startActivity(userFeed);
    }
    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from User Results" + result);
        if (result != null) {
            Gson gson = new Gson();
             registerDTO = gson.fromJson(result, RegisterDTO.class);
            if(registerDTO != null && requestSentValue.equals(registerDTO.getRequestSent())){
                sendRequest.setVisibility(View.GONE);
                sentText.setVisibility(View.VISIBLE);
                sentText.setText("you already sended a Request");
            }
            //toolbar.setTitle(registerDTO.getFirstName() + "  " + registerDTO.getLastName());
            if(registerDTO.getArea()!=null) {
                userLocation.setText(registerDTO.getArea());
            }
            if(registerDTO.getImgPath()!=null) {
                if(registerDTO.getImgPath().contains("https")){
                    glide.with(context).load(registerDTO.getImgPath()).into(userImage);
                    Log.i(tag,"https image view");
                }
                else {
                   // setFileToDownload(registerDTO.getImgPath());
                    glide.with(context).load(Config.S3_URL+registerDTO.getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);
                    Log.i(tag,"set to file down load image view");
                }
            }

        }
    }
    public void  setValuesTogroupTextFields(String result) {
        System.out.println("json xxxx from User Results" + result);
        if (result!=null){
            Gson gson = new Gson();
            RegisterDTO fromJsonn = gson.fromJson(result, RegisterDTO.class);



            }
        }


    private RegisterDTO buildDTOObject() {
        RegisterDTO newRegisterDTO= new RegisterDTO();
        newRegisterDTO.setToUserId(registerDTO.getUserId());
        newRegisterDTO.setToFirstName(registerDTO.getFirstName());
        newRegisterDTO.setToLastName(registerDTO.getLastName());
        newRegisterDTO.setUserId(fromUserId);
        newRegisterDTO.setFirstName(fromFirstName);
        newRegisterDTO.setLastName(fromLastName);
        return newRegisterDTO;
    }


    private boolean checkValidation() {
        boolean ret = true;

        return ret;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

