package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.PadiPoojaView;
import ayyappa.eloksolutions.in.ayyappaap.RestServices;


public class EventViewHelper {

    private PadiPoojaView mcontext;

    public EventViewHelper(PadiPoojaView mcontext){
        this.mcontext = mcontext;
    }

    public class Eventview extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        private ProgressDialog progress;
        public Eventview(String surl){
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
    public class JoinEvent extends AsyncTask<String, String, String> {
        // Call after onPreExecute method
        URL url;
        String surl;
        EventMembers eventMembers;

        private ProgressDialog progress;
        public JoinEvent(EventMembers eventMembers,String surl){
            this.eventMembers = eventMembers;
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
                jsonObject.accumulate("padiId", eventMembers.getPadiId());
                jsonObject.accumulate("userId", eventMembers.getUserId());
                jsonObject.accumulate("firstName", eventMembers.getFirstName());
                jsonObject.accumulate("lastName", eventMembers.getLastName());
                jsonObject.accumulate("padiName", eventMembers.getPadiName());
                json = jsonObject.toString();
                System.out.println("json values"+json);
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
    public class LeaveEvent extends AsyncTask<String, Void, String> {
        URL url;
        String surl;
        private ProgressDialog progress;
        public LeaveEvent(String surl){
            this.surl = surl;
        }
        protected void onPreExecute(){
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
                System.out.println(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RestServices.GET(url);
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            progress.dismiss();
        }
    }
    public class DeleteEvent extends AsyncTask<String, Void, String> {
        URL url;
        String surl;
        private ProgressDialog progress;
        public DeleteEvent(String surl){
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
                System.out.println("Connection to url ................." + surl);
                url = new URL(surl);
                System.out.println(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return RestServices.GET(url);
        }
        protected void onPostExecute(String result) {
            progress.dismiss();
           // Toast.makeText(getActivity().getBaseContext(), "Deleted " + event_name.getText() + " event", Toast.LENGTH_LONG).show();
            //mcontext.startActivity(new Intent(mcontext, MyEventsTabBar.class));
        }

    }
}
