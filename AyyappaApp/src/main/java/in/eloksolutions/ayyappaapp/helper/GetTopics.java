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


import in.eloksolutions.ayyappaapp.recycleviews.MyRecyclerTopic;
import in.eloksolutions.ayyappaapp.util.RestServices;
import in.eloksolutions.ayyappaapp.beans.TopicDTO;
import in.eloksolutions.ayyappaapp.util.TopicObject;


public class GetTopics extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvPadi;

    public GetTopics(Context mcontext, String surl, RecyclerView rvPadi) {
        this.mcontext = mcontext;
        this.surl=surl;
        this.rvPadi=rvPadi;
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
            System.out.println("Get Topic Result is "+result);
            progress.dismiss();
            if (result!=null && result.trim().length()>0) {
                Gson gson = new Gson();
                Type type = new TypeToken<List<TopicDTO>>() { }.getType();
                List<TopicDTO> fromJson = gson.fromJson(result, type);
                ArrayList results = new ArrayList<TopicObject>();

                String events[]=new String[fromJson.size()];

                for (TopicDTO event : fromJson) {
                    TopicObject obj = new TopicObject(event.getGroupId(),event.getTopicId(), event.getTopic(),event.getDescription(),event.getOwner(),event.getOwnerName(),event.getImgPath(),event.getsCreateDate());
                    results.add(obj);
                }
                MyRecyclerTopic mAdapter = new MyRecyclerTopic(results,mcontext);
                rvPadi.setAdapter(mAdapter);
            }
        }

}
