package in.eloksolutions.ayyappaapp.beans;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import java.net.URL;

import in.eloksolutions.ayyappaapp.activities.UserView;
import in.eloksolutions.ayyappaapp.util.RestServices;


/**
 * Created by welcome on 8/23/2017.
 */

public class UserGroupsHelper {
    private UserView mcontext;

    public UserGroupsHelper(UserView mcontext) {
        this.mcontext = mcontext;
    }

    public class UserViewTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;

        public UserViewTask(String surl) {
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
            try {
                url = new URL(surl);
            } catch (Exception e) {
            }
            return RestServices.GET(url);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                mcontext.setValuesToTextFields(result);
            }
            System.out.println("event Tpic Topicview" + result);
            progress.dismiss();

        }
    }
}

