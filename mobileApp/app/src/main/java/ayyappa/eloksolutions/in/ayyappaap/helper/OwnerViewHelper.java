package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.OwnerView;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;

/**
 * Created by welcome on 7/6/2017.
 */

public class OwnerViewHelper {

    private OwnerView mcontext;
    public OwnerViewHelper(OwnerView mcontext) {
        this.mcontext = mcontext;
    }

    public class UserViewTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;
        public UserViewTask(String surl){
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







