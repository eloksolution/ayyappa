package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.roughike.bottombar.BottomBar;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetGroups;

public class GroupList extends CardViewActivity {
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
               Intent groupCreate= new Intent(GroupList.this, CreateGroup.class);
                startActivity(groupCreate);
            }
        });
        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"group/getgroups";
        GetGroups getGroups=new GetGroups(context,url,rvGroups);
        System.out.println("url for group list"+url);
        getGroups.execute();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home1:
                        Intent intent1 = new Intent(GroupList.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:

                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(GroupList.this, PadiPoojaFull.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(GroupList.this, MapsActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(GroupList.this, OwnerView.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

    }


    }


