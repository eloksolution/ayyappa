package in.eloksolutions.ayyappaapp.activities;

import android.content.Intent;
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
import in.eloksolutions.ayyappaapp.recycleviews.ServicesGridView;

public class Movis extends AppCompatActivity {
    GridView grid;
    ImageView add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.submenu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("Movies List");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        add = (ImageView) findViewById(R.id.add);

        final String[] services = new String[] {"Ayyappa Swamy Janma Rahasyam Telugu Movie | Latest Tollywood Movies | Telugu Devotional Cinema", "అయ్యప్ప స్వామి మహత్యం తెలుగు సినిమా ఉపశీర్షికలుతో | శరత్ బాబు | సిల్క్ స్మిత | కె వాసు", "Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas", "Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji", "Engal Swamy Ayyappan full Movie HD"};
        String [] Images={
               "vxpEMuM1eBc",
                "hRtuGEQmm1E",
                "4wjuDG7WXY8",
                "FTBLd2zz8IU",
                "o4vv3PN45Eo",
                "TQkGgSqNOOo"

        };
        ServicesGridView adapter = new ServicesGridView(Movis.this, services, Images);
        grid=(GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +array[+ position], Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        String uri = "vxpEMuM1eBc";
                        Intent intent = new Intent(Movis.this,PlayYoutubeActivity.class);
                        intent.putExtra("uri",uri);
                        startActivity(intent);
                        break;
                    case 1:
                        String uri1 = "hRtuGEQmm1E";
                        Intent intent1 = new Intent(Movis.this,PlayYoutubeActivity.class);
                        intent1.putExtra("uri",uri1);
                        startActivity(intent1);
                        break;
                    case 2:
                        String uri2 = "4wjuDG7WXY8";
                        Intent intent2 = new Intent(Movis.this,PlayYoutubeActivity.class);
                        intent2.putExtra("uri",uri2);
                        startActivity(intent2);
                        break;
                    case 3:
                        String uri3 = "FTBLd2zz8IU";
                        Intent intent3 = new Intent(Movis.this,PlayYoutubeActivity.class);
                        intent3.putExtra("uri",uri3);
                        startActivity(intent3);
                        break;
                    case 4:
                        String uri4 = "o4vv3PN45Eo";
                        Intent intent4 = new Intent(Movis.this,PlayYoutubeActivity.class);
                        intent4.putExtra("uri",uri4);
                        startActivity(intent4);
                        break;
                    case 5:
                        String uri5 = "TQkGgSqNOOo";
                        Intent intent5 = new Intent(Movis.this,PlayYoutubeActivity.class);
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
            case R.id.action_settings:
                Intent home=new Intent(Movis.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
