package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.PadiPoojaUpdate;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;
import ayyappa.eloksolutions.in.ayyappaap.beans.EventDTO;

/**
 * Created by welcome on 7/6/2017.
 */

public class PadiUpdateHelper {

    private PadiPoojaUpdate mcontext;
    public PadiUpdateHelper(PadiPoojaUpdate mcontext) {
        this.mcontext = mcontext;
    }

    public class PadiUpdateTask extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;
        public PadiUpdateTask(String surl){
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
            System.out.println("event from eventview" + result);
            progress.dismiss();

        }
    }
    public class PadiUpdateHere extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        EventDTO eventDTO;

        private ProgressDialog progress;
        public PadiUpdateHere(EventDTO eventDTO, String surl){
            this.eventDTO = eventDTO;
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
            String json = "";
            try {
                url = new URL(surl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("padipoojaId", eventDTO.getPadipoojaId());
                jsonObject.accumulate("eventName", eventDTO.getEventName());
                jsonObject.accumulate("location", eventDTO.getLocation());
                jsonObject.accumulate("description", eventDTO.getDescription());
                jsonObject.accumulate("date", eventDTO.getDate());
                jsonObject.accumulate("time", eventDTO.getTime());
                jsonObject.accumulate("memId", eventDTO.getMemId());
                jsonObject.accumulate("name", eventDTO.getOwnerName());
                jsonObject.accumulate("imgPath", eventDTO.getImagePath());

                json = jsonObject.toString();
                System.out.println("json padipooja Update values"+json);
            } catch (Exception e) {
            }
            return RestServices.POST(url, json);
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            System.out.println("From Join Event" + result);
            progress.dismiss();
            mcontext.callBackUpdateUI(eventDTO.getPadipoojaId());
        }
    }





    }

