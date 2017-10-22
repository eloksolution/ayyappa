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
import in.eloksolutions.ayyappaapp.activities.SwamiRequest;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerViewSwamiRequests;
import in.eloksolutions.ayyappaapp.util.DataObjectRequests;
import in.eloksolutions.ayyappaapp.util.RestServices;


/**
 * Created by welcome on 6/30/2017.
 */

public class GetSwamiRequests extends AsyncTask<String, Void, String> {
    private SwamiRequest swamiRequest;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvGroups;

    public GetSwamiRequests(SwamiRequest swamiRequest, String surl, RecyclerView rvGroups) {
        this.swamiRequest = swamiRequest;
        this.surl = surl;
        this.rvGroups = rvGroups;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(swamiRequest);
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
        System.out.println("Get Groups Result is " + result);
        progress.dismiss();
        if (result != null && result.trim().length() > 0) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<RegisterDTO>>() {
            }.getType();
            List<RegisterDTO> fromJson = gson.fromJson(result, type);
            ArrayList<DataObjectRequests> results = new ArrayList<DataObjectRequests>();
            for (RegisterDTO requests : fromJson) {
                DataObjectRequests obj = new DataObjectRequests(requests.getUserId(), requests.getImgPath(), requests.getFirstName(), requests.getLastName(), R.drawable.yes, R.drawable.no);
                results.add(obj);
            }
            MyRecyclerViewSwamiRequests mAdapter = new MyRecyclerViewSwamiRequests(results, swamiRequest);
            rvGroups.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
        }
    }
}
