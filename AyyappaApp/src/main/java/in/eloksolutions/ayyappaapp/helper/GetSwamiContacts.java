package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.ContactActivity;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerViewSwamiContacts;
import in.eloksolutions.ayyappaapp.util.RestServices;


/**
 * Created by welcome on 6/30/2017.
 */

public class GetSwamiContacts extends AsyncTask<String, Void, String> {
    private ContactActivity contactActivity;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvGroups;

    public GetSwamiContacts(ContactActivity contactActivity, String surl, RecyclerView rvGroups) {
        this.contactActivity = contactActivity;
        this.surl = surl;
        this.rvGroups = rvGroups;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       /*progress = new ProgressDialog(contactActivity);
        progress.setMessage("Loading...");
        progress.show();*/
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
        System.out.println("Get Groups Result is " + result);
   //     progress.dismiss();
        if (result != null && result.trim().length() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RegisterDTO>>() {
            }.getType();
            List<RegisterDTO> fromJson = gson.fromJson(result, type);
            ArrayList<GroupMemberObject> results = new ArrayList<GroupMemberObject>();
            for (RegisterDTO m : fromJson) {
                GroupMemberObject mem = new GroupMemberObject(m.getUserId(), m.getFirstName(), R.drawable.ayyappa_logo, m.getLastName(),m.getImgPath());
                results.add(mem);
            }
            MyRecyclerViewSwamiContacts mAdapter = new MyRecyclerViewSwamiContacts(results, contactActivity);
            rvGroups.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
