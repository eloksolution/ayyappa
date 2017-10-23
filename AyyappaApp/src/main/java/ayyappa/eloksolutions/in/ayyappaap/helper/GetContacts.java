package ayyappa.eloksolutions.in.ayyappaap.helper;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;

import java.net.URL;

import ayyappa.eloksolutions.in.ayyappaap.RestServices;

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
        /*
        if (result!=null && result.trim().length()>0) {
            Gson gson = new Gson();
           Type type = new TypeToken<List<MemberDTO>>() { }.getType();
          List<MemberDTO> fromJson = gson.fromJson(result, type);
            ArrayList results = new ArrayList<DataObject>();
            String events[]=new String[fromJson.size()];
            int i=0;
            for (MemberDTO event : fromJson) {
                events[i] = event.getMemName();
                System.out.println("contact name "+events[i]);
                DataObject obj = new DataObject(events[i],event.getMemId()+"" , R.drawable.chat_icon);
                results.add(i, obj);
                i++;
            }
            MyRecyclerViewAdapter mAdapter = new MyRecyclerViewAdapter(results);
            rvPadi.setAdapter(mAdapter);
        }*/
    }

}
