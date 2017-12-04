package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.roughike.bottombar.BottomBar;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GetSearchSwamiRequests;
import in.eloksolutions.ayyappaapp.util.Util;


public class SearchSwamiRequest extends AppCompatActivity {
    Context context;
    AmazonS3 s3;
   TransferUtility transferUtility;
    String userId,userName;
    private BottomBar bottomBar;
    String Tag;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Swami Search List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        context=this;
        TextView noData=(TextView) findViewById(R.id.tv_no_data);
        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES,MODE_PRIVATE);
        userId=preferences.getString("userId",null);
        userName=preferences.getString("firstName",null)+preferences.getString("lastName",null);
       String searcValue= getIntent().getStringExtra("searchValue");
        Log.i(Tag,"The value in Search swami requests"+searcValue);
        String url= Config.SERVER_URL+"user/search/"+searcValue;
        GetSearchSwamiRequests getGroups=new GetSearchSwamiRequests(SearchSwamiRequest.this,url,rvGroups,noData);
        System.out.println("url for group list"+url);
        getGroups.execute();

      /*  BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home1:
                        Intent intent1 = new Intent(SearchSwamiRequest.this, CardViewActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_groups:

                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(SearchSwamiRequest.this, PadiPoojaFull.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(SearchSwamiRequest.this, MapsMarkerActivity.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(SearchSwamiRequest.this, OwnerView.class);
                        startActivity(intent4);
                        break;
                }
                return false;
            }
        });*/

    }
    public void contacList( String userId) {
       Intent groupView = new Intent(SearchSwamiRequest.this, UserView.class);
        groupView.putExtra("swamiUSerId", userId);
        startActivity(groupView);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;
            case R.id.feed:
                Intent feed=new Intent(SearchSwamiRequest.this, FeedBackForm.class);
                startActivity(feed);
                return true;
            case R.id.share:
                startActivity(Util.getInviteIntent(userName));
                return true;
            case R.id.action_settings:
                Intent home=new Intent(SearchSwamiRequest.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


