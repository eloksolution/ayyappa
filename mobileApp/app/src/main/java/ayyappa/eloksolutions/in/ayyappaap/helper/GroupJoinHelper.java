package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.GroupView;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.GroupMembers;

/**
 * Created by welcome on 7/6/2017.
 */

public class GroupJoinHelper {

    private Context mcontextt;
    public GroupJoinHelper(Context mcontextt) {
        this.mcontextt = mcontextt;
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
            progress = new ProgressDialog(mcontextt);
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
            Intent groupView=new Intent(mcontextt, GroupView.class);
            groupView.putExtra("groupId",groupMembers.getGroupId());
            mcontextt.startActivity(groupView);
            System.out.println("From Join Event" + result);
            progress.dismiss();

        }
    }





    }

