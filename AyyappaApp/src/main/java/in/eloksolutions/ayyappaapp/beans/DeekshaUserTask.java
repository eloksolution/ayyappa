package in.eloksolutions.ayyappaapp.beans;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import in.eloksolutions.ayyappaapp.util.RestServices;
import in.eloksolutions.ayyappaapp.config.Config;

public class DeekshaUserTask extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        private ProgressDialog progress;
    String Tag="DeekshaUserTask";
    String startdate;
    String enddate;
    String description;
    String memId;
    Context context;

    public DeekshaUserTask(Context context, String startdate, String enddate, String description, String memId){

        this.startdate=startdate;
        this.enddate=enddate;
        this.description=description;
        this.memId=memId;
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
                String surl = Config.SERVER_URL + "deeksha_save.php";
                Log.i(Tag,"Connection to url ................." + surl);
                url = new URL(surl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("startdate", startdate);
                jsonObject.accumulate("memid", memId);
                jsonObject.accumulate("enddate", enddate);
                jsonObject.accumulate("description", description);
                json = jsonObject.toString();
                Log.i(Tag,"Jason is "+json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RestServices.POST(url, json);
        }
    protected void onPostExecute(String result) {
        Log.i(Tag,"Response is "+result);
        progress.dismiss();
    }
}

