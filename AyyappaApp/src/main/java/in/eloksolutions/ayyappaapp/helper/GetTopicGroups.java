package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


import in.eloksolutions.ayyappaapp.beans.GroupDTO;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerViewGroup;
import in.eloksolutions.ayyappaapp.util.DataObjectGroup;
import in.eloksolutions.ayyappaapp.util.RestServices;


/**
 * Created by welcome on 6/30/2017.
 */

public class GetTopicGroups extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvGroups;
    AmazonS3 s3;
    TransferUtility transferUtility;
    String Tag="topic list";

    public GetTopicGroups(Context mcontext, String surl, RecyclerView rvGroups, AmazonS3 s3, TransferUtility transferUtility) {
        this.mcontext = mcontext;
        this.surl=surl;
        this.rvGroups=rvGroups;
        this.s3=s3;
        this.transferUtility=transferUtility;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progress = new ProgressDialog(mcontext);
        progress.setMessage("Loading...");
        progress.show();
    }

    protected String doInBackground(String... urls) {
        URL url = null;
        try {
            System.out.println("Connection to url ................." + surl);
            url = new URL(surl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestServices.GET(url);
    }

    protected void onPostExecute(String result) {
        System.out.println("Get Groups Result is "+result);
        progress.dismiss();
        if (result!=null && result.trim().length()>0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<GroupDTO>>() { }.getType();
            List<GroupDTO> fromJson = gson.fromJson(result, type);
            ArrayList<DataObjectGroup> results = new ArrayList<DataObjectGroup>();
             for (GroupDTO group : fromJson) {
                 List<RegisterDTO> groupMembers=group.getGroupMembers();
                 int memberSize=0;
                 if(groupMembers !=null)
                     memberSize=groupMembers.size();
                 DataObjectGroup obj = new DataObjectGroup(group.getName(),group.getDescription(), group.getImagePath(), group.getGroupid(),memberSize, group.getOwner(), group.getIsMember());
                results.add(obj);
           }

            MyRecyclerViewGroup mAdapter = new MyRecyclerViewGroup(results,mcontext,s3,transferUtility);
            rvGroups.setAdapter(mAdapter);
        }
    }

}
