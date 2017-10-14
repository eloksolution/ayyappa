package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import in.eloksolutions.ayyappaapp.util.RestServices;
import in.eloksolutions.ayyappaapp.beans.DiscussionDTO;

/**
 * Created by welcome on 6/28/2017.
 */

public class DiscussionHelper {
    private Context mcontext;
    public DiscussionHelper(Context mcontext) {
        this.mcontext = mcontext;
    }
    String tag="DissHelper";

    public class CreateDiscussion extends AsyncTask<String, Void, String> {
        // Call after onPreExecute method
        URL url;
        DiscussionDTO discussionDto;
        private ProgressDialog progress;
        String gurl;

        public CreateDiscussion(DiscussionDTO discussionDto, String gurl) {
            this.discussionDto = discussionDto;
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
                System.out.println("Connection topic to url ................." + gurl);
                url = new URL(gurl);
                JSONObject jsonObject = new JSONObject();
                jsonObject.accumulate("topicId", discussionDto.getTopicId());
                jsonObject.accumulate("comment", discussionDto.getComment());
                jsonObject.accumulate("ownerId", discussionDto.getOwnerId());
                jsonObject.accumulate("ownerName", discussionDto.getOwnerName());
                json = jsonObject.toString();
                System.out.println("Json discussion helper is" + json);

            }
            catch (Exception e) {
                e.printStackTrace();
            }

            String result= RestServices.POST(url, json);
            System.out.println("Response  is" + result);
            return result;
        }
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            Log.i(tag, "result is " +result);
            progress.dismiss();
        }

    }
}
