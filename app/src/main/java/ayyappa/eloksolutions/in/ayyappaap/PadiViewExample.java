package ayyappa.eloksolutions.in.ayyappaap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;

import ayyappa.eloksolutions.in.ayyappaap.beans.EventDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.FlexiViewHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.PadiObject;

/**
 * Created by welcome on 7/24/2017.
 */

public class PadiViewExample extends AppCompatActivity {
    RecyclerView rvPadi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.padi_example);

        rvPadi = (RecyclerView) findViewById(R.id.rv_members);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        FlexiViewHelper eventViewHelper = new FlexiViewHelper(this);
        String surl = Config.SERVER_URL + "padipooja/padipoojaEdit/"+"596db580a4ffc92fdb259af8";
        try {
            String output=eventViewHelper.new Eventview(surl).execute().get();
            System.out.println("the output from PadiPooja"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}

    }
    public void setValuesToTextFields(String result) {
        System.out.println("json from eventview" + result);
        if (result != null) {
            Gson gson = new Gson();
            EventDTO fromJson = gson.fromJson(result, EventDTO.class);
            if (fromJson.getPadiMembers() != null) {
                ArrayList results = new ArrayList<PadiObject>();
                for (RegisterDTO m : fromJson.getPadiMembers()) {

                    PadiObject mem = new PadiObject(m.getUserId(), m.getFirstName(), R.drawable.ayyappa_logo, m.getLastName());
                    results.add(mem);

                }
                MyRecyclerPadiMembers mAdapter = new MyRecyclerPadiMembers(results);
                rvPadi.setAdapter(mAdapter);
                System.out.println("object resul myrecycler results list view is " + results);
            }
        }
    }
}