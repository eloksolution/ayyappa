package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.net.URL;

import in.eloksolutions.ayyappaapp.activities.TopicView;
import in.eloksolutions.ayyappaapp.util.RestServices;


/**
 * Created by welcome on 7/6/2017.
 */

public class TopicViewHelper {

    private TopicView mcontext;
    public TopicViewHelper(TopicView mcontext) {
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

    }







