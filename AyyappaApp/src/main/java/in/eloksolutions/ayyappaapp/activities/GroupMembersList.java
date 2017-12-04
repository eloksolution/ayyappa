package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;

import java.util.ArrayList;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.GroupDTO;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GroupMemberHelper;
import in.eloksolutions.ayyappaapp.helper.GroupMemberObject;
import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerGroupListMembers;
import in.eloksolutions.ayyappaapp.util.Util;


public class GroupMembersList extends AppCompatActivity  {
    Context context;
    RecyclerView rvPadi;
    String groupId,userId,userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_member_list);
        context=this;
        groupId=getIntent().getStringExtra("groupId");
       Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
       getSupportActionBar().setTitle("Group Members");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        userId=preferences.getString("userId",null);
        userName=preferences.getString("firstName",null)+preferences.getString("lastName",null);
        //ImageView createGroup=(ImageView) findViewById(R.id.add);

        GroupMemberHelper getGroupsValue=new GroupMemberHelper(this);
        String surl = Config.SERVER_URL+"group/getgroup/"+groupId+"/"+userId;
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new GroupViewTask(surl).execute().get();
            System.out.println("the output from Group"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}

        rvPadi = (RecyclerView) findViewById(R.id.rvgroup_members);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);

    }

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from groupview" + result);
        if (result != null || result.length() == 0) {
            Gson gson = new Gson();
            GroupDTO fromJsonn = gson.fromJson(result, GroupDTO.class);
            if (fromJsonn.getGroupMembers()!=null) {
                ArrayList results = new ArrayList<GroupMemberObject>();
                for (RegisterDTO m : fromJsonn.getGroupMembers()) {

                    GroupMemberObject mem = new GroupMemberObject(m.getUserId(), m.getFirstName(), R.drawable.ayyappa_logo, m.getLastName(),m.getImgPath());
                    results.add(mem);

                }
                MyRecyclerGroupListMembers mAdapter = new MyRecyclerGroupListMembers(results,context);
                rvPadi.setAdapter(mAdapter);
                System.out.println("object resul myrecycler results list view is " + results);
            }

            System.out.println("json xxxx from fromJsonn.getGroupMembers().size()" + fromJsonn.getGroupMembers().size());

        }
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
            case R.id.feed:
                Intent feed=new Intent(GroupMembersList.this, FeedBackForm.class);
                startActivity(feed);
                return true;
            case R.id.share:
                startActivity(Util.getInviteIntent(userName));
                return true;
            case R.id.action_settings:
                Intent home=new Intent(GroupMembersList.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
