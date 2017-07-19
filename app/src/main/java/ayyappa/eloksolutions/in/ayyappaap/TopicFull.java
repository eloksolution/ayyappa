package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetTopics;

/**
 * Created by welcome on 1/4/2017.
 */

public class TopicFull extends AppCompatActivity implements View.OnClickListener {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topicall);
        context=this;
        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvPadi);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"topic/getTopics";
        GetTopics getEvents=new GetTopics(context,url,rvPadi);
        getEvents.execute();
    }
    public void onClick(View v) {

        if (v == v) {
            Intent intent = new Intent(getBaseContext(), PadiPoojaView.class);

            startActivity(intent);
        }

        }
}
