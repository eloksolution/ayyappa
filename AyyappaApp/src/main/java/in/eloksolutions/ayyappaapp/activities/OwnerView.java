package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.BottomNavigationViewHelper;
import in.eloksolutions.ayyappaapp.helper.OwnerViewHelper;
import in.eloksolutions.ayyappaapp.maps.MapsMarkerActivity;


/**
 * Created by welcome on 6/30/2017.
 */

public class OwnerView extends AppCompatActivity {
    ImageView userImage;
    TextView userName, userLocation;
    Context context;
    String userId;
    RegisterDTO registerDTO;
    Glide glide;
    TextView contacts;
    Toolbar toolbar;
    String tag = "TopicView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myactivities_listview);
        userName = (TextView) findViewById(R.id.user_name);
        userLocation = (TextView) findViewById(R.id.user_location);
        contacts = (TextView) findViewById(R.id.user_contacts);
        TextView groups = (TextView) findViewById(R.id.user_groups);
        userImage = (ImageView) findViewById(R.id.user_image);
        context = this;
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        userId = preferences.getString("userId", null);
        Log.i(tag, "userId preferences.getString is" + userId);
        final Context ctx = this;
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Swami Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        OwnerViewHelper gettopicValue = new OwnerViewHelper(this);
        String surl = Config.SERVER_URL + "user/user/" + userId+"/"+userId;
        System.out.println("url for group topic view list" + surl);
        try {
            String output = gettopicValue.new UserViewTask(surl).execute().get();
            System.out.println("the output from Topic" + output);
            setValuesToTextFields(output);
            System.out.println("registerDTO.getImgPath()" + registerDTO.getImgPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FloatingActionButton userUpDate = (FloatingActionButton) findViewById(R.id.fabuser);
        userUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent topicUp = new Intent(ctx, UserUpdate.class);
                topicUp.putExtra("userId", "" + userId);
                startActivity(topicUp);
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_home1:
                        Intent intent1 = new Intent(OwnerView.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:
                        Intent intent4 = new Intent(OwnerView.this, GroupList.class);
                        startActivity(intent4);
                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(OwnerView.this, PadiPoojaFull.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(OwnerView.this, MapsMarkerActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:

                        break;
                }


                return false;
            }
        });

    }

    public void userContacts(View view) {
        Intent topicUp = new Intent(this, ContactActivity.class);
        topicUp.putExtra("userId", "" + userId);
        startActivity(topicUp);


    }

    public void userGroups(View view) {
        Intent topicUp = new Intent(this, UserGroups.class);
        topicUp.putExtra("userId", "" + userId);
        startActivity(topicUp);
    }

    public void userPadipooja(View view) {
        Intent topicUp = new Intent(this, UserPadiPoojas.class);
        topicUp.putExtra("userId", "" + userId);
        startActivity(topicUp);

    }

    public void userInvite(View view) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, registerDTO.getFirstName() + " is invite to ayyappaApp" + " www.eloksolutions.com");
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    public void userFeedBack(View view) {
        Intent userFeed = new Intent(this, FeedBackForm.class);
        userFeed.putExtra("userId", "" + userId);
        startActivity(userFeed);
    }
    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from User Results" + result);
        if (result != null) {
            Gson gson = new Gson();
            registerDTO = gson.fromJson(result, RegisterDTO.class);
            userName.setText(registerDTO.getFirstName() + "  " + registerDTO.getLastName());
            if(registerDTO.getCity()!=null || registerDTO.getArea()!=null) {
                userLocation.setText(registerDTO.getCity() + ", " + registerDTO.getArea());
            }
            toolbar.setTitle(registerDTO.getFirstName() + "  " + registerDTO.getLastName());
            if (registerDTO.getImgPath() != null) {
                if (registerDTO.getImgPath().contains("http")) {
                    glide.with(context).load(registerDTO.getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);
                } else {
                    glide.with(context).load(Config.S3_URL + registerDTO.getImgPath()).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);

                }

            }else{
                glide.with(context).load(R.drawable.defaulta).diskCacheStrategy(DiskCacheStrategy.ALL).into(userImage);

            }
        }

    }
    private boolean checkValidation() {
        boolean ret = true;


        return ret;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

