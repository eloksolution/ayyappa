package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;


import in.eloksolutions.ayyappaapp.activities.GroupMembersList;
import in.eloksolutions.ayyappaapp.util.RestServices;
import in.eloksolutions.ayyappaapp.beans.GroupMembers;

/**
 * Created by welcome on 7/6/2017.
 */

public class GroupMemberHelper {

    private GroupMembersList mcontext;
    public GroupMemberHelper(GroupMembersList mcontext) {
        this.mcontext = mcontext;
    }

    public class GroupViewTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;
        public GroupViewTask(String surl){
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
            System.out.println("event from eventview" + result);
            progress.dismiss();

        }
    }
    public class JoinGroup extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        GroupMembers groupMembers;

        private ProgressDialog progress;
        public JoinGroup(GroupMembers groupMembers, String surl){
            this.groupMembers = groupMembers;
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
            String json = "";
            try {
                url = new URL(surl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("groupId", groupMembers.getGroupId());
                jsonObject.accumulate("userId", groupMembers.getUserId());
                jsonObject.accumulate("firstName", groupMembers.getFirstname());
                jsonObject.accumulate("lastName", groupMembers.getLastName());
                json = jsonObject.toString();
                System.out.println("json values"+json);
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

