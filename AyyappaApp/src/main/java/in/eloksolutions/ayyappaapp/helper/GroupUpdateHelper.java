package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;


import in.eloksolutions.ayyappaapp.util.RestServices;
import in.eloksolutions.ayyappaapp.beans.GroupDTO;
import in.eloksolutions.ayyappaapp.activities.GroupUpdate;


/**
 * Created by welcome on 7/6/2017.
 */

public class GroupUpdateHelper {

    private GroupUpdate mcontext;

    public GroupUpdateHelper(GroupUpdate mcontext) {
        this.mcontext = mcontext;
    }

    public class GroupUpdateTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;

        public GroupUpdateTask(String surl) {
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
            System.out.println("Group Udate from GroupView" + result);
            progress.dismiss();

        }
    }

    public class GroupUpdateHere extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        GroupDTO groupDTO;

        private ProgressDialog progress;

        public GroupUpdateHere(GroupDTO groupDTO, String surl) {
            this.groupDTO = groupDTO;
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
                jsonObject.accumulate("groupId", groupDTO.getGroupid());
                jsonObject.accumulate("name", groupDTO.getName());
                jsonObject.accumulate("description", groupDTO.getDescription());
                jsonObject.accumulate("type", groupDTO.getType());
                jsonObject.accumulate("imagePath", groupDTO.getImagePath());
                jsonObject.accumulate("catgory", groupDTO.getGroupCatagory());
                json = jsonObject.toString();
                System.out.println("json At Group Updated Time values :: " + json);
            } catch (Exception e) {
            }
            return RestServices.POST(url, json);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            System.out.println("json At Group Updated Time values :" + result);
            progress.dismiss();

        }
    }


}

