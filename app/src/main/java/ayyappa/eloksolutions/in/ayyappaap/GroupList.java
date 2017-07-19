package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetGroups;

public class GroupList extends AppCompatActivity implements View.OnClickListener {
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_list);
        context=this;
       Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //ImageView createGroup=(ImageView) findViewById(R.id.add);

      /*  final Context ctx = this;
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupView = new Intent(ctx, CreateGroup.class);
                startActivity(groupView);
            }
        }); */

        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"group/getgroups";
        GetGroups getGroups=new GetGroups(context,url,rvGroups);
        System.out.println("url for group list"+url);
        getGroups.execute();

    }

    public void onClick(View v) {

        if (v == v) {
            Intent intent = new Intent(getBaseContext(), GroupView.class);

            startActivity(intent);
        }

    }

}
