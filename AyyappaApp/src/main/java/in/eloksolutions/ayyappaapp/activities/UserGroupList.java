package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GetGroups;
import in.eloksolutions.ayyappaapp.maps.MapsMarkerActivity;
import in.eloksolutions.ayyappaapp.util.Util;

public class UserGroupList extends CardViewActivity {
    Context context;
private BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);
        context=this;
       Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Group List");

        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        String deekshaStartDate=preferences.getString("startDate",null);
        String deekshaEndDate=preferences.getString("endDate",null);
        userId=preferences.getString("userId",null);
        userName=preferences.getString("firstName",null)+preferences.getString("lastName",null);
        //ImageView createGroup=(ImageView) findViewById(R.id.add);

      /*  final Context ctx = this;
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupView = new Intent(ctx, CreateGroup.class);
                startActivity(groupView);
            }
        }); */
        TextView noData=(TextView) findViewById(R.id.tv_no_data);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabgroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent groupCreate= new Intent(UserGroupList.this, CreateGroup.class);
                startActivity(groupCreate);
            }
        });
        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"group/getgroups";
        GetGroups getGroups=new GetGroups(context,url,rvGroups, noData);
        System.out.println("url for group list"+url);
        getGroups.execute();

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_calls) {



                } else if (tabId == R.id.tab_groups) {

                    Intent i = new Intent(getBaseContext(), UserGroupList.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_chats) {
                    Intent i = new Intent(getBaseContext(), PadiPoojaFull.class);
                    startActivity(i);

                }
                else if (tabId == R.id.tab_home) {

                    Intent i = new Intent(getBaseContext(), MapsMarkerActivity.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_profile) {
                    Intent regiser=new Intent(getBaseContext(), UserView.class);
                    startActivity(regiser);


                }
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.action_settings:
                Intent home=new Intent(UserGroupList.this, CardViewActivity.class);
                startActivity(home);
                return true;
            case R.id.feed:
                Intent feed=new Intent(UserGroupList.this, FeedBackForm.class);
                startActivity(feed);
                return true;
            case R.id.share:
                startActivity(Util.getInviteIntent(userName));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    }


