package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import in.eloksolutions.ayyappaapp.beans.EventDTO;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerViewAdapterHome;
import in.eloksolutions.ayyappaapp.util.DataObjectPadiPooja;
import in.eloksolutions.ayyappaapp.util.RestServices;


public class GetEventsHome extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvPadi;
    TextView noData;

    public GetEventsHome(Context mcontext, String surl, RecyclerView rvPadi, TextView noData) {
        this.mcontext = mcontext;
        this.surl=surl;
        this.rvPadi=rvPadi;
        this.noData=noData;
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
            System.out.println("Get Events Result is "+result);
            progress.dismiss();
            if (result!=null && result.trim().length()>0) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<EventDTO>>() { }.getType();
                List<EventDTO> fromJson = gson.fromJson(result, type);
                ArrayList results = new ArrayList<DataObjectPadiPooja>();
                for (EventDTO event : fromJson) {
                    List<RegisterDTO> padiMembers=event.getPadiMembers();
                    int memberSize=0;
                    if(padiMembers !=null)
                        memberSize=padiMembers.size();
                    DataObjectPadiPooja obj = new DataObjectPadiPooja(event.getEventName(),event.getDescription(), event.getImagePath(),event.getPadipoojaId(),memberSize,event.getDate(),event.getLocation(),event.getMonth(),event.getDay(),event.getWeek());
                    results.add(obj);
                }
                if(!results.isEmpty()) {
                    MyRecyclerViewAdapterHome mAdapter = new MyRecyclerViewAdapterHome(results, mcontext);
                    rvPadi.setAdapter(mAdapter);
                    rvPadi.setHasFixedSize(true);
                }else{
                    noData.setVisibility(View.VISIBLE);
                }
            }
        }
}
