package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetGroups;

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


        //ImageView createGroup=(ImageView) findViewById(R.id.add);

      /*  final Context ctx = this;
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupView = new Intent(ctx, CreateGroup.class);
                startActivity(groupView);
            }
        }); */
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
        GetGroups getGroups=new GetGroups(context,url,rvGroups, s3, transferUtility);
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

                    Intent i = new Intent(getBaseContext(), MapsActivity.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_profile) {
                    Intent regiser=new Intent(getBaseContext(), UserView.class);
                    startActivity(regiser);


                }
            }
        });

    }


    }


