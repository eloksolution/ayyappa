package in.eloksolutions.ayyappaapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.recycleviews.ServicesGridView;
import in.eloksolutions.ayyappaapp.util.Util;

public class Songs extends AppCompatActivity {
    GridView grid;
    ImageView add;
    Toolbar toolbar;
    String firstName,lastName,userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submenu);
        toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Songs List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add = (ImageView) findViewById(R.id.add);
        SharedPreferences preferences=getSharedPreferences(Config.APP_PREFERENCES,MODE_PRIVATE);
        userId= preferences.getString("userId",null);
        firstName=preferences.getString("firstName",null);
        lastName=preferences.getString("lastName",null);

        final String[] services = new String[] {"Ayyappa janmarahasyam", "Ayyappa Swamy Mahatyam", "Ayyappa Telugu ", "Ayyappa Swamy Maha Sarath Babu", "Ayyappa Deeksha Telugu", "Ayyappa Swamy Janma Rahasyam"};
        int [] Images={
                R.drawable.ayy1,
                R.drawable.ayy2,
                R.drawable.ayy3,
                R.drawable.ayy4,
                R.drawable.ayy5,
                R.drawable.ayy

        };
        ServicesGridView adapter = new ServicesGridView(Songs.this, services, Images);
        grid=(GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +array[+ position], Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        String uri = "https://www.youtube.com/watch?v=ZRYJdPrHiSM";
                        Intent intent = new Intent(Songs.this,WebActivity.class);
                        intent.putExtra("uri",uri);
                        startActivity(intent);
                        break;
                    case 1:
                        String uri1 = "https://www.youtube.com/watch?v=zV0lDPtAUxw";
                        Intent intent1 = new Intent(Songs.this,WebActivity.class);
                        intent1.putExtra("uri",uri1);
                        startActivity(intent1);
                        break;
                    case 2:
                        String uri2 = "https://www.youtube.com/watch?v=nquYSlnavuM";
                        Intent intent2 = new Intent(Songs.this,WebActivity.class);
                        intent2.putExtra("uri",uri2);
                        startActivity(intent2);
                        break;
                    case 3:
                        String uri3 = "https://www.youtube.com/watch?v=pNGdT5obEys";
                        Intent intent3 = new Intent(Songs.this,WebActivity.class);
                        intent3.putExtra("uri",uri3);
                        startActivity(intent3);
                        break;
                    case 4:
                        String uri4 = "https://www.youtube.com/watch?v=tzLX8me67wU";
                        Intent intent4 = new Intent(Songs.this,WebActivity.class);
                        intent4.putExtra("uri",uri4);
                        startActivity(intent4);
                        break;
                    case 5:
                        String uri5 = "https://www.youtube.com/watch?v=BOjJGALm2kQ";
                        Intent intent5 = new Intent(Songs.this,WebActivity.class);
                        intent5.putExtra("uri",uri5);
                        startActivity(intent5);
                        break;
                    default:
                        break;

                }
            }

        });

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
                Intent feed=new Intent(Songs.this, FeedBackForm.class);
                startActivity(feed);
                return true;
            case R.id.share:
                startActivity(Util.getInviteIntent(firstName+" "+lastName));
                return true;
            case R.id.action_settings:
                Intent home=new Intent(Songs.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
