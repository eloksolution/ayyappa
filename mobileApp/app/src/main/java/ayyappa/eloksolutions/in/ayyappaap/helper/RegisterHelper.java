package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.MainActivity;
import ayyappa.eloksolutions.in.ayyappaap.Registartion;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.activity.LoginActivity;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;

/**
 * Created by welcome on 6/28/2017.
 */

public class RegisterHelper {
    private Registartion mcontext;
    private LoginActivity loginActivity;
    public RegisterHelper(Registartion mcontext) {
        this.mcontext = mcontext;
    }

    public RegisterHelper(LoginActivity mcontext) {
        this.loginActivity = mcontext;
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
            if(loginActivity!=null){
                progress = new ProgressDialog(loginActivity);
            }else {
                progress = new ProgressDialog(mcontext);
            }
            progress.setMessage("Loading...");
            progress.show();
        }

        protected String doInBackground(String... urls) {
            String json = "";
            try {
                System.out.println("Connection to url ................." + gurl);
                url = new URL(gurl);
                JSONObject jsonObject = new JSONObject();
                if(!Util.isEmpty(registerDto.getFirstName()))
                    jsonObject.accumulate("firstName", registerDto.getFirstName());
                if(!Util.isEmpty(registerDto.getLastName()))
                    jsonObject.accumulate("lastName", registerDto.getLastName());
                if(!Util.isEmpty(registerDto.getMobile()))
                    jsonObject.accumulate("mobile", registerDto.getMobile());
                if(!Util.isEmpty(registerDto.getEmail()))
                jsonObject.accumulate("email", registerDto.getEmail());
                if(!Util.isEmpty(registerDto.getArea()))
                jsonObject.accumulate("area", registerDto.getArea());
                if(!Util.isEmpty(registerDto.getCity()))
                jsonObject.accumulate("city", registerDto.getCity());
                if(!Util.isEmpty(registerDto.getState()))
                jsonObject.accumulate("state", registerDto.getState());
                if(!Util.isEmpty(registerDto.getPassword()))
                jsonObject.accumulate("password",registerDto.getPassword());
                if(registerDto.getLongi()>0)
                jsonObject.accumulate("lon",registerDto.getLongi());
                if(registerDto.getLati()>0)
                jsonObject.accumulate("lat",registerDto.getLati());
                if(!Util.isEmpty(registerDto.getImgPath()))
                jsonObject.accumulate("imgPath", registerDto.getImgPath());
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
            progress.dismiss();
            Log.i(tag, "User id  " +result);
            if (result!=null&&!result.startsWith("ERROR")) {
                registerDto.setUserId(result);
                if(loginActivity!=null){
                    loginActivity.updateUI(true,registerDto);
                    return;
                }

                mcontext.redirectToHome(registerDto);
            }else{
                Toast.makeText(mcontext, "Not able to Registered", Toast.LENGTH_LONG).show();
            }
            Log.i(tag, "result is  " +result);
        }
    }
}
