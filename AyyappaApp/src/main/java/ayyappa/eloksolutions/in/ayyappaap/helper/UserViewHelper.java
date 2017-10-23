package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.UserView;

/**
 * Created by welcome on 7/6/2017.
 */

public class UserViewHelper {

    private UserView mcontext;
    public UserViewHelper(UserView mcontext) {
        this.mcontext = mcontext;
    }

    public class UserViewTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        UserView userView;
        private ProgressDialog progress;
        public UserViewTask(UserView userView, String surl){
            this.surl = surl;
            this.userView=userView;
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







