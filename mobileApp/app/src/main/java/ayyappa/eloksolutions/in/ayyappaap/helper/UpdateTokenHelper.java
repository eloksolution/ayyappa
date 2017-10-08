package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.maps.MapsMarkerActivity;

/**
 * Created by welcome on 6/28/2017.
 */

public class UpdateTokenHelper extends AsyncTask<String, Void, String> {

    String TAG = "UpdateTokenHelper";
    URL url;
    String userId;
    String token;
    String gurl;
    public UpdateTokenHelper(String gurl, String userId,String token) {
        this.gurl = gurl;
        this.userId=userId;
        this.token=token;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    protected String doInBackground(String... urls) {
        String result=null;
        try {
            String surl = Config.SERVER_URL + "deeksha_save.php";
            Log.i(TAG,"Connection to url ................." + surl);
            url = new URL(surl);
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("userId", userId);
            jsonObject.accumulate("fcmToken", token);

            String json = jsonObject.toString();
            Log.i(TAG,"Jason is "+json);
            RestServices.POST(url, json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success";

    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(TAG, "result is " + result);
          }
}
