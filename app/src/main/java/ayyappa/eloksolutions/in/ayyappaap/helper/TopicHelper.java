package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDTO;

/**
 * Created by welcome on 6/28/2017.
 */

public class TopicHelper {
    private Context mcontext;
    public TopicHelper(Context mcontext) {
        this.mcontext = mcontext;
    }
    String tag="GroupHelper";

    public class CreateTopic extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        TopicDTO topicDto;
        private ProgressDialog progress;
        String gurl;

        public CreateTopic(TopicDTO topicDto, String gurl) {
            this.topicDto = topicDto;
            this.gurl = gurl;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(mcontext);
            progress.setMessage("Loading...");
            progress.show();
        }

        protected String doInBackground(String... urls) {
            String json = "";
            try {
                System.out.println("Connection to url ................." + gurl);
                url = new URL(gurl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("groupId", topicDto.getGroupId());
                jsonObject.accumulate("owner", topicDto.getOwner());
                jsonObject.accumulate("topic", topicDto.getTopic());
                jsonObject.accumulate("description", topicDto.getDescription());
                json = jsonObject.toString();
                System.out.println("Json is" + json);

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            String result= RestServices.POST(url, json);
            System.out.println("Response  is" + result);
            return result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i(tag, "result is " +result);
            progress.dismiss();
        }

    }
}
