package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetEvents;

/**
 * Created by welcome on 1/4/2017.
 */

public class PadiPoojaFull extends AppCompatActivity implements View.OnClickListener {
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.padipoojafull);
        context=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvPadi);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"padipooja/getpadipooja";
        GetEvents getEvents=new GetEvents(context,url,rvPadi);
        getEvents.execute();
    }
    public void onClick(View v) {

        if (v == v) {
            Intent intent = new Intent(getBaseContext(), PadiPoojaView.class);

            startActivity(intent);
        }

        }
}
