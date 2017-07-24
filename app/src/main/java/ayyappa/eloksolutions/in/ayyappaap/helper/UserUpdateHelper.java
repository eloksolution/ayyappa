package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.UserUpdate;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;

/**
 * Created by welcome on 7/6/2017.
 */

public class UserUpdateHelper {

    private UserUpdate mcontext;
    public UserUpdateHelper(UserUpdate mcontext) {
        this.mcontext = mcontext;
    }

    public class UserUpdateTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;
        public UserUpdateTask(String surl){
            this.surl = surl;
        }
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            progress = new ProgressDialog(mcontext);
            progress.setMessage("Loading...");
            progress.show();
        }
        protected String doInBackground(String... urls) {
            try {
                url = new URL(surl);
            } catch (Exception e) {
            }
            return RestServices.GET(url);
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result!=null){
                mcontext.setValuesToTextFields(result);
            }
            System.out.println("event Tpic userUpdate" + result);
            progress.dismiss();

        }
    }

    public class TopicUpdateHere extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        RegisterDTO registerDTO;

        private ProgressDialog progress;

        public TopicUpdateHere(RegisterDTO registerDTO, String surl) {
            this.registerDTO = registerDTO;
            this.surl = surl;
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
                url = new URL(surl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("userId", registerDTO.getUserId());
                jsonObject.accumulate("firstName", registerDTO.getFirstName());
                jsonObject.accumulate("lastName", registerDTO.getLastName());
                jsonObject.accumulate("mobile", registerDTO.getMobile());
                jsonObject.accumulate("email", registerDTO.getEmail());
                jsonObject.accumulate("area", registerDTO.getArea());
                jsonObject.accumulate("city", registerDTO.getCity());
                jsonObject.accumulate("state", registerDTO.getState());
                jsonObject.accumulate("password", registerDTO.getPassword());

                json = jsonObject.toString();
                System.out.println("json values" + json);
            } catch (Exception e) {
            }
            return RestServices.POST(url, json);
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            System.out.println("From Join Event" + result);
            progress.dismiss();

        }

    }
    }







