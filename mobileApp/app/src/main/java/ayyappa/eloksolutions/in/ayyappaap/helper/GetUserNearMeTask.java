package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.DiscussionDTO;
import ayyappa.eloksolutions.in.ayyappaap.maps.MapsMarkerActivity;

/**
 * Created by welcome on 6/28/2017.
 */

public class GetUserNearMeTask extends AsyncTask<String, Void, String> {
    private MapsMarkerActivity mcontext;
    String tag = "GetUserNearMeTask";
    URL url;
    private ProgressDialog progress;
    String gurl;

    public GetUserNearMeTask(String gurl, MapsMarkerActivity mcontext) {
        this.gurl = gurl;
        this.mcontext=mcontext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(mcontext);
        progress.setMessage("Loading...");
        progress.show();
    }

    protected String doInBackground(String... urls) {
        String result=null;
        try {
            System.out.println("Connection topic to url ................." + gurl);
            url = new URL(gurl);
            result = RestServices.GET(url);
           Log.i(tag,"Response  is" + result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        Log.i(tag, "result is " + result);
        progress.dismiss();
        mcontext.callBackPinUsers(result);
    }
}
