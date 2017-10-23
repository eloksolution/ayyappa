package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.TopicUpdate;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDTO;

/**
 * Created by welcome on 7/6/2017.
 */

public class TopicUpdateHelper {

    private TopicUpdate mcontext;
    public TopicUpdateHelper(TopicUpdate mcontext) {
        this.mcontext = mcontext;
    }

    public class TopicViewTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;
        public TopicViewTask(String surl){
            this.surl = surl;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progress = new ProgressDialog(mcontext);
            progress.setMessage("Loading...");
            progress.show();
        }
        protected String doInBackground(String... urls) {
            try {
                url = new URL(surl);
            } catch (Exception e) {
            }
            return RestServices.GET(url);
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result!=null){
                mcontext.setValuesToTextFields(result);
            }
            System.out.println("event Tpic Topicview" + result);
            progress.dismiss();

        }
    }

    public class TopicUpdateHere extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        TopicDTO topicDTO;

        private ProgressDialog progress;

        public TopicUpdateHere(TopicDTO topicDTO, String surl) {
            this.topicDTO = topicDTO;
            this.surl = surl;
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
                url = new URL(surl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("topicId", topicDTO.getTopicId());
                jsonObject.accumulate("topic", topicDTO.getTopic());
                jsonObject.accumulate("description", topicDTO.getDescription());

                json = jsonObject.toString();
                System.out.println("json values" + json);
            } catch (Exception e) {
            }
            return RestServices.POST(url, json);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            System.out.println("From Join Event" + result);
            progress.dismiss();

        }

    }
    }







