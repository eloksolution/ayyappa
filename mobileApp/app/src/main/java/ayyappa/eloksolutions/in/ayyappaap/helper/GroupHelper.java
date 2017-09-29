package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.CreateGroup;
import ayyappa.eloksolutions.in.ayyappaap.GroupList;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.GroupDTO;

/**
 * Created by welcome on 6/28/2017.
 */

public class GroupHelper {
    private CreateGroup mcontext;
    public GroupHelper(CreateGroup mcontext) {
        this.mcontext = mcontext;
    }
    String tag="GroupHelper";

    public class CreateGroupTask extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        GroupDTO groupDto;
        private ProgressDialog progress;
        String gurl;

        public CreateGroupTask(GroupDTO groupDto, String gurl) {
            this.groupDto = groupDto;
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
                jsonObject.accumulate("name", groupDto.getName());
                jsonObject.accumulate("description", groupDto.getDescription());
                jsonObject.accumulate("type", groupDto.getType());
                jsonObject.accumulate("owner", groupDto.getOwner());
                jsonObject.accumulate("createDate", groupDto.getCreateDate());
                jsonObject.accumulate("numberOfMembers", groupDto.getNumberOfMembers());
                jsonObject.accumulate("imagePath", groupDto.getImagePath());
                jsonObject.accumulate("catgory",groupDto.getGroupCatagory());
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
            Log.i(tag, "result is " +result);
            super.onPostExecute(result);
            mcontext.callbackCreateGroup(result);

            Log.i(tag, "result groupId is " +result);
            progress.dismiss();
        }

    }
}
