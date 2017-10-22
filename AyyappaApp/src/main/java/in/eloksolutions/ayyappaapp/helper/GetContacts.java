package in.eloksolutions.ayyappaapp.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.net.URL;

import in.eloksolutions.ayyappaapp.util.RestServices;

/**
 * Created by welcome on 1/4/2017.
 */

public class GetContacts extends AsyncTask<String, Void, String> {
    private Context mcontext;
    private ProgressDialog progress;
    String surl;
    RecyclerView rvPadi;

    public GetContacts(Context mcontext, String surl, RecyclerView rvPadi) {
        this.mcontext = mcontext;
        this.surl=surl;
        this.rvPadi=rvPadi;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
       progress = new ProgressDialog(mcontext);
       progress.setMessage("Loading...");
       progress.show();
    }

    protected String doInBackground(String... urls) {
        URL url = null;
        try {
            System.out.println("Connection to url ................." + surl);
            url = new URL(surl);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return RestServices.GET(url);
    }

    protected void onPostExecute(String result) {
        System.out.println("Get Contact Result is "+result);
        progress.dismiss();

        if (result!=null && result.trim().length()>0) {
            Gson gson = new Gson();


        }
    }

}
