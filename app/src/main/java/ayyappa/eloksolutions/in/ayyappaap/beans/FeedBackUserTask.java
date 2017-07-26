package ayyappa.eloksolutions.in.ayyappaap.beans;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.CheckInternet;
import ayyappa.eloksolutions.in.ayyappaap.MainActivity;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;

public class FeedBackUserTask extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        private ProgressDialog progress;
        String name;
    String phoneNumber;
    String emailId;
    String comments;
    Context context;



    public FeedBackUserTask(Context context, String name, String phoneNumber, String emailId, String comments){
            this.name=name;
        this.phoneNumber=phoneNumber;
        this.emailId=emailId;
        this.comments=comments;
        this.context=context;
        }
        protected void onPreExecute(){
            super.onPreExecute();
            progress = new ProgressDialog(context);
            progress.setMessage("Loading...");
            progress.show();
        }
        protected String doInBackground(String... urls) {
            String json = "";
            try {
                String surl = Config.SERVER_URL + "feedback/add";
                System.out.println("Connection to url ................." + surl);
                url = new URL(surl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("name", name);
                jsonObject.accumulate("mobile", phoneNumber);
                jsonObject.accumulate("email", emailId);
                jsonObject.accumulate("feedback", comments);
                json = jsonObject.toString();
                System.out.println(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RestServices.POST(url, json);
        }
    protected void onPostExecute(String result) {
        SharedPreferences preferences = context.getSharedPreferences(Config.User_ID, context.MODE_PRIVATE);
        SharedPreferences.Editor edit;
        edit= preferences.edit();
        if(!result.equals("")) {
            String id = result;
            edit.putString("memId", id);
            edit.putString("name", name);
            edit.putString("mobile", phoneNumber);
            edit.putString("email", emailId);
            edit.putString("feedback",comments);
            edit.commit();
        }else{
            CheckInternet.showAlertDialog(context, "Invalid Details",
                    "Error while registering the user please send feedback form with your details.");
        }
        progress.dismiss();
        Intent feedbaciInt = new Intent(context, MainActivity.class);
        context.startActivity(feedbaciInt);
    }
}

