package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.beans.GroupDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GroupMemberHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.GroupMemberObject;

public class GroupMembersList extends AppCompatActivity implements View.OnClickListener {
    Context context;
    RecyclerView rvPadi;
    String groupId;

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

        //ImageView createGroup=(ImageView) findViewById(R.id.add);

      /*  final Context ctx = this;
        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupView = new Intent(ctx, CreateGroup.class);
                startActivity(groupView);
            }
        }); */

        GroupMemberHelper getGroupsValue=new GroupMemberHelper(this);
        String surl = Config.SERVER_URL+"group/getgroup/"+groupId+"/598839b6e4b0ca1af7a13b";
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

    public void onClick(View v) {

        if (v == v) {

        }

    }
    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from groupview" + result);
        if (result != null) {
            Gson gson = new Gson();
            GroupDTO fromJsonn = gson.fromJson(result, GroupDTO.class);
            if (fromJsonn.getGroupMembers()!=null) {
                ArrayList results = new ArrayList<GroupMemberObject>();
                for (RegisterDTO m : fromJsonn.getGroupMembers()) {

                    GroupMemberObject mem = new GroupMemberObject(m.getUserId(), m.getFirstName(), R.drawable.ayyappa_logo, m.getLastName());
                    results.add(mem);

                }
                MyRecyclerGroupListMembers mAdapter = new MyRecyclerGroupListMembers(results);
                rvPadi.setAdapter(mAdapter);
                System.out.println("object resul myrecycler results list view is " + results);
            }

            System.out.println("json xxxx from fromJsonn.getGroupMembers().size()" + fromJsonn.getGroupMembers().size());

        }
    }

}
