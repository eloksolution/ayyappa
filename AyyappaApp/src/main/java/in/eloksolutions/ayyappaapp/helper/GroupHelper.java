package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.net.URL;


import in.eloksolutions.ayyappaapp.activities.GroupList;
import in.eloksolutions.ayyappaapp.util.RestServices;
import in.eloksolutions.ayyappaapp.beans.GroupDTO;


/**
 * Created by welcome on 6/28/2017.
 */

public class GroupHelper {
    private Context mcontext;
    public GroupHelper(Context mcontext) {
        this.mcontext = mcontext;
    }
    String tag="GroupHelper";

    public class CreateGroup extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        GroupDTO groupDto;
        private ProgressDialog progress;
        String gurl;

        public CreateGroup(GroupDTO groupDto, String gurl) {
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

            try {
                FirebaseMessaging.getInstance().subscribeToTopic(result);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent groupView = new Intent(mcontext, GroupList.class);
            mcontext.startActivity(groupView);
            Log.i(tag, "result groupId is " +result);
            progress.dismiss();
        }

    }
}
