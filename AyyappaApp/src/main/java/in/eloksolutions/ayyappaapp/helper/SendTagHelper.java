package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import in.eloksolutions.ayyappaapp.activities.UserView;
import in.eloksolutions.ayyappaapp.util.RestServices;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;

/**
 * Created by welcome on 6/28/2017.
 */

public class SendTagHelper {
    private Context mcontext;
    public SendTagHelper(Context mcontext) {
        this.mcontext = mcontext;
    }
    String tag="GroupHelper";

    public class SenTagTask extends AsyncTask<String, Void, String> {
        URL url;
        RegisterDTO registerDto;
        private ProgressDialog progress;
        String gurl;

        public SenTagTask(RegisterDTO registerDto, String gurl) {
            this.registerDto = registerDto;
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
                jsonObject.accumulate("userId",registerDto.getUserId());
                jsonObject.accumulate("firstName", registerDto.getFirstName());
                jsonObject.accumulate("lastName", registerDto.getLastName());
                jsonObject.accumulate("connectedToId", registerDto.getToUserId());
                jsonObject.accumulate("toFirstName", registerDto.getToFirstName());
                jsonObject.accumulate("toLastName", registerDto.getToLastName());
                json = jsonObject.toString();
                System.out.println("Json Send Tag Helper is :: " + json);

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            String result= RestServices.POST(url, json);
            System.out.println("Response send Tag helper is :: " + result);
            return result;
        }
        protected void onPostExecute(String result) {
            Log.i(tag, "result Response send Tag helper is ::  " +result);
            progress.dismiss();
            Intent userView=new Intent(mcontext, UserView.class);
            userView.putExtra("swamiUserId",registerDto.getToUserId());
            mcontext.startActivity(userView);

        }

    }
}
