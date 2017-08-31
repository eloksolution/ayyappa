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
import ayyappa.eloksolutions.in.ayyappaap.helper.GetEvents;

/**
 * Created by welcome on 1/4/2017.
 */

public class PadiPoojaFull extends CardViewActivity {
    Context context;
    private BottomBar bottomBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.padipoojafull);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvPadi);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"padipooja/getpoojas";
        GetEvents getEvents=new GetEvents(context,url,rvPadi);
        getEvents.execute();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_padi);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupCreate= new Intent(PadiPoojaFull.this, CreatePadiPooja.class);
                startActivity(groupCreate);
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
                    Intent regiser=new Intent(getBaseContext(), OwnerView.class);
                    startActivity(regiser);


                }
            }
        });
    }

}
