package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.EventDTO;

public class CreatePadiPoojaHelper {
    private Context mcontext;
    public CreatePadiPoojaHelper(Context mcontext) {
        this.mcontext = mcontext;
    }
    String tag="CreatePadiPoojaHelper";

    public class CreateEvent extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        EventDTO eventDTO;
        private ProgressDialog progress;
        String surl;

        public CreateEvent(EventDTO eventDTO, String surl) {
            this.eventDTO = eventDTO;
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
                System.out.println("Connection to url ................." + surl);
                url = new URL(surl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("eventName", eventDTO.getEventName());
                jsonObject.accumulate("location", eventDTO.getLocation());
                jsonObject.accumulate("description", eventDTO.getDescription());
                jsonObject.accumulate("date", eventDTO.getDate());
                jsonObject.accumulate("time", eventDTO.getTime());
                jsonObject.accumulate("memId", eventDTO.getMemId());
                jsonObject.accumulate("name", eventDTO.getOwnerName());
                json = jsonObject.toString();
                System.out.println("Json is" + json);
            } catch (Exception e) {
                e.printStackTrace();
            }

            String result= RestServices.POST(url, json);
            System.out.println("Response  is" + result);
            System.out.println("Response json is" + json);
            System.out.println("Response url is" + url);
            return result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            String eventid=result;
            progress.dismiss();

        }

    }
}
