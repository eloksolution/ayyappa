package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;

/**
 * Created by welcome on 6/28/2017.
 */

public class RegisterHelper {
    private Context mcontext;
    public RegisterHelper(Context mcontext) {
        this.mcontext = mcontext;
    }
    String tag="GroupHelper";

    public class CreateRegistration extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        RegisterDTO registerDto;
        private ProgressDialog progress;
        String gurl;

        public CreateRegistration(RegisterDTO registerDto, String gurl) {
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
                jsonObject.accumulate("firstName", registerDto.getFirstName());
                jsonObject.accumulate("lastName", registerDto.getLastName());
                jsonObject.accumulate("mobile", registerDto.getMobile());
                jsonObject.accumulate("email", registerDto.getEmail());
                jsonObject.accumulate("area", registerDto.getArea());
                jsonObject.accumulate("city", registerDto.getCity());
                jsonObject.accumulate("state", registerDto.getState());
                jsonObject.accumulate("password",registerDto.getPassword());
                jsonObject.accumulate("lon",registerDto.getLongi());
                jsonObject.accumulate("lat",registerDto.getLati());

                //  jsonObject.accumulate("loc",registerDto.getLati());
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
            if (result!=null&&!result.startsWith("ERROR")) {
                registerDto.setUserId(result);
                Util.setPreferances(mcontext, registerDto);
                Log.i(tag, "result is registerDto " +registerDto);
            }else{
                Toast.makeText(mcontext, "Not able to Registered", Toast.LENGTH_LONG).show();
            }
            Log.i(tag, "result is  " +result);
            progress.dismiss();
        }

    }
}
