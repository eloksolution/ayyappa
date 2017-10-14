package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.widget.Toast;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GetContacts;


public class ContactActivity extends AppCompatActivity {
    Context context;
    String memId,memName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactactivity);
        SharedPreferences preferences = getSharedPreferences(Config.userId, Context.MODE_PRIVATE);


      SearchView searchView=(SearchView) findViewById(R.id.searchView);
        searchView.setQueryHint("Search View");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                Toast.makeText(getBaseContext(), query, Toast.LENGTH_LONG).show();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Toast.makeText(getBaseContext(), newText, Toast.LENGTH_LONG).show();
                return false;
            }
        });

        memId = preferences.getString("memId", null);
        memName= preferences.getString("memName", null);
        context=this;

        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvContacts);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"get_contacts.php?memId="+memId;
        GetContacts getContacts=new GetContacts(context,url,rvPadi);
        getContacts.execute();

    }
    }




