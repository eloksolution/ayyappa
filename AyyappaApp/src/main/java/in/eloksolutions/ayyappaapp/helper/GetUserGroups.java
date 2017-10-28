package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.eloksolutions.ayyappaapp.beans.GroupDTO;
import in.eloksolutions.ayyappaapp.beans.UserDTO;
import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerViewTopicGroup;
import in.eloksolutions.ayyappaapp.util.DataObjectGroup;
import in.eloksolutions.ayyappaapp.util.RestServices;


/**
 * Created by welcome on 6/30/2017.
 */

public class GetUserGroups extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvGroups;

    public GetUserGroups(Context mcontext, String surl, RecyclerView rvGroups) {
        this.mcontext = mcontext;
        this.surl=surl;
        this.rvGroups=rvGroups;
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
        System.out.println("Get user Groups Result is "+result);
        progress.dismiss();
        if (result!=null && result.trim().length()>0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<UserDTO>>() { }.getType();
            List<UserDTO> fromJson = gson.fromJson(result, type);
            ArrayList<DataObjectGroup> results = new ArrayList<DataObjectGroup>();
             for (UserDTO group : fromJson) {
                 List<GroupDTO> userGroups=group.getGroups();
                 int memberSize=0;
                 if(userGroups !=null)
                     memberSize=userGroups.size();


           }
            if (!results.isEmpty()) {

                MyRecyclerViewTopicGroup mAdapter = new MyRecyclerViewTopicGroup(results, mcontext);
                rvGroups.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }else{
                System.out.println("Get else Grouping  Result is "+results);
            }
        }
    }

}
