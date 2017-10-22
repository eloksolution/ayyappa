package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.net.URL;

import in.eloksolutions.ayyappaapp.activities.SwamiRequest;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.util.RestServices;


/**
 * Created by welcome on 6/28/2017.
 */

public class SendAcceptTask extends AsyncTask<String, Void, String> {
    URL url;
    RegisterDTO registerDto;
    private ProgressDialog progress;
    String gurl;
    SwamiRequest swamiRequest;
    String tag="SendAcceptTask";

    public SendAcceptTask(RegisterDTO registerDto, String gurl, SwamiRequest swamiRequest) {
        this.registerDto = registerDto;
        this.gurl = gurl;
        this.swamiRequest=swamiRequest;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(swamiRequest);
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
        swamiRequest.contacList(registerDto.getUserId());

    }
}
