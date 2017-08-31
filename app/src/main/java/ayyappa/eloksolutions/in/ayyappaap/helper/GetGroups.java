package ayyappa.eloksolutions.in.ayyappaap.helper;

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

import ayyappa.eloksolutions.in.ayyappaap.MyRecyclerViewGroup;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.GroupDTO;
import ayyappa.eloksolutions.in.ayyappaap.util.DataObjectGroup;


/**
 * Created by welcome on 6/30/2017.
 */

public class GetGroups extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvGroups;

    public GetGroups(Context mcontext, String surl, RecyclerView rvGroups) {
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
        System.out.println("Get Groups Result is "+result);
        progress.dismiss();
        if (result!=null && result.trim().length()>0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<GroupDTO>>() { }.getType();
            List<GroupDTO> fromJson = gson.fromJson(result, type);
            ArrayList results = new ArrayList<DataObjectGroup>();
             for (GroupDTO group : fromJson) {
                 DataObjectGroup obj = new DataObjectGroup(group.getName(),group.getDescription(), group.getImagePath(), group.getGroupid());
                results.add(obj);

            }
            MyRecyclerViewGroup mAdapter = new MyRecyclerViewGroup(results);
            rvGroups.setAdapter(mAdapter);
        }
    }

}
