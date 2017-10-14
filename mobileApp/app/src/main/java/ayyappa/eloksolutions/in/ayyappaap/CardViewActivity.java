package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetEventsHome;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetGroups;
import ayyappa.eloksolutions.in.ayyappaap.util.DataObject;


public class CardViewActivity extends AppCompatActivity {

    ListView lv;
    Context context;

    String TAG="AudioPlayer";
    GridView grid;
    ImageView add;
    public static int [] moviesImages ={R.drawable.ayy1,R.drawable.ayy2,R.drawable.ayy3,R.drawable.ayy4,R.drawable.ayy5,R.drawable.ayy};
    public static String [] moviesNames={"Ayyappa Swamy Janma Rahasyam Telugu Movie 2014","Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan","Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith","Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas","Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji","Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};
    public static String [] moviesid={"vxpEMuM1eBc","hRtuGEQmm1E","4wjuDG7WXY8","FTBLd2zz8IU","o4vv3PN45Eo","TfT8w5v8KSY"};
    AmazonS3 s3;
    TransferUtility transferUtility;
    RecyclerView mRecyclerView;
    private static String LOG_TAG = "CardViewActivity";
    TextView topic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        // Set up the ViewPager with the sections adapter.
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.ic_home1:
                        break;

                    case R.id.ic_groups:
                        Intent intent1 = new Intent(CardViewActivity.this, GroupList.class);
                        startActivity(intent1);
                        break;

                    case R.id.ic_books:
                        Intent intent2 = new Intent(CardViewActivity.this, PadiPoojaFull.class);
                        startActivity(intent2);
                        break;

                    case R.id.ic_center_focus:
                        Intent intent3 = new Intent(CardViewActivity.this, MapView.class);
                        startActivity(intent3);
                        break;

                    case R.id.ic_backup:
                        Intent intent4 = new Intent(CardViewActivity.this, OwnerView.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        final String[] services = new String[] {"Ayyappa janmarahasyam", "Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan", "Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith" };
        int [] Images={
                R.drawable.ayy1,
                R.drawable.ayy2,
                R.drawable.ayy3,


        };
        ServicesGridView adapter = new ServicesGridView(CardViewActivity.this, services, Images);
        grid=(GridView)findViewById(R.id.gridview);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +array[+ position], Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        String uri = "https://www.youtube.com/watch?v=vxpEMuM1eBc";
                        Intent intent = new Intent(CardViewActivity.this,WebActivity.class);
                        intent.putExtra("uri",uri);
                        startActivity(intent);
                        break;
                    case 1:
                        String uri1 = "https://www.youtube.com/watch?v=hRtuGEQmm1E";
                        Intent intent1 = new Intent(CardViewActivity.this,WebActivity.class);
                        intent1.putExtra("uri",uri1);
                        startActivity(intent1);
                        break;
                    case 2:
                        String uri2 = "https://www.youtube.com/watch?v=4wjuDG7WXY8";
                        Intent intent2 = new Intent(CardViewActivity.this,WebActivity.class);
                        startActivity(intent2);
                        break;
                    default:
                        break;

                }
            }

        });



        context=this;
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        String deekshaStartDate=preferences.getString("startDate",null);
        String deekshaEndDate=preferences.getString("endDate",null);

        final ImageView imgDeeksha=(ImageView) findViewById(R.id.event_image);
        final TextView tvDays=(TextView) findViewById(R.id.topic);
        Log.i(TAG,"Deeksha Config.getUserId()"+preferences.getString("userId", null));
        Log.i(TAG,"Deeksha Config.getUserName()"+preferences.getString("lastName", null));
        ImageView editDeksha=(ImageView) findViewById(R.id.edit_deeksha);
        editDeksha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent editde=new Intent(CardViewActivity.this, DeekshaActivity.class);

                startActivity(editde);


            }
        });
        topic=(TextView) findViewById(R.id.topic);

        if(deekshaStartDate!=null){
            Log.i(TAG,"Deeksha start date"+deekshaStartDate);
            int diff=0,noOfDays=0;
            try {
                Date startDate=(new SimpleDateFormat("dd/MM/yyyy")).parse(deekshaStartDate);
                Date endDate=(new SimpleDateFormat("dd/MM/yyyy")).parse(deekshaEndDate);
                Calendar cal=Calendar.getInstance();
                Date today=cal.getTime();
                diff=daysBetween(startDate,today)+1;
                noOfDays=daysBetween(startDate,endDate)+1;
                Log.i(TAG,"Diff date is is "+diff);
              final LinearLayout deekshaLayout= (LinearLayout)findViewById(R.id.event_item);
                final TextView lStartMonth=(TextView) deekshaLayout.findViewById(R.id.month);
                final TextView lStartDate=(TextView) deekshaLayout.findViewById(R.id.date);
                final TextView lStartWeek=(TextView) deekshaLayout.findViewById(R.id.day);
                cal.setTime(startDate);
                Log.i(TAG,"start date is"+deekshaStartDate+" date "+cal.get(Calendar.DAY_OF_MONTH));
                 lStartMonth.setText(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH ));
                lStartDate.setText(cal.get(Calendar.DAY_OF_MONTH)+"");
                lStartWeek.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH ));


                final TextView lEndMonth=(TextView) deekshaLayout.findViewById(R.id.month_r);
                final TextView lEndDate=(TextView) deekshaLayout.findViewById(R.id.date_r);
                final TextView lEndWeek=(TextView) deekshaLayout.findViewById(R.id.day_r);
                cal.setTime(endDate);
                lEndMonth.setText(cal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.ENGLISH ));
                lEndDate.setText(cal.get(Calendar.DAY_OF_MONTH)+"");
                Log.i(TAG,"week d"+cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH ));
                lEndWeek.setText(cal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.ENGLISH ));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            imgDeeksha.setVisibility(View.GONE);
            topic.setText(diff+"/"+noOfDays+"");
        }else{
            tvDays.setText("Start Deeksha");
            imgDeeksha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, DeekshaActivity.class));
                }
            });

        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabgroup);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupCreate= new Intent(CardViewActivity.this, CreateTopic.class);
                startActivity(groupCreate);
            }
        });


        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvPadi_home);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"padipooja/gettoppoojas";
        GetEventsHome getEvents=new GetEventsHome(context,url,rvPadi);
        getEvents.execute();


        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups_home);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager groups = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(groups);
        String gurl= Config.SERVER_URL+"group/getfirstgroups";
        GetGroups getGroups=new GetGroups(context,gurl,rvGroups);
        System.out.println("url for group list"+gurl);
        getGroups.execute();

        final ImageView padiFull=(ImageView) findViewById(R.id.PadiFull);
        padiFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent padipooj = new Intent(context,PadiPoojaFull.class);
                startActivity(padipooj);
            }
        });
        final ImageView padiCreate=(ImageView) findViewById(R.id.padi_create);
        padiCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent padipooj = new Intent(context,CreatePadiPooja.class);
                startActivity(padipooj);
            }
        });
        final ImageView movieFull=(ImageView) findViewById(R.id.movie_full);
        movieFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,Movis.class);
                startActivity(songsIntent);
            }
        });
        final ImageView CreateGroup=(ImageView) findViewById(R.id.group_list);
        CreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,GroupList.class);
                startActivity(songsIntent);
            }
        });
        final ImageView GroupsFull=(ImageView) findViewById(R.id.create_group);
        GroupsFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,CreateGroup.class);
                startActivity(songsIntent);
            }
        });
        final ImageView songsFull=(ImageView) findViewById(R.id.songs_view);
        songsFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,Songs.class);
                startActivity(songsIntent);
            }
        });
        final String[] services_one = new String[] {"Ayyappa janmarahasyam", "Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan", "Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith", "Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas", "Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji", "Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};
        int [] Images_one={
                R.drawable.ayy1,
                R.drawable.ayy2,
                R.drawable.ayy3,
                R.drawable.ayy4,
                R.drawable.ayy5,
                R.drawable.ayy

        };
        ServicesGridView adapter1 = new ServicesGridView(CardViewActivity.this, services_one, Images_one);
        grid=(GridView)findViewById(R.id.gridview_one);
        grid.setAdapter(adapter1);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "You Clicked at " +array[+ position], Toast.LENGTH_SHORT).show();
                switch (position) {
                    case 0:
                        String uri = "https://www.youtube.com/watch?v=vxpEMuM1eBc";
                        Intent intent = new Intent(CardViewActivity.this,WebActivity.class);
                        intent.putExtra("uri",uri);
                        startActivity(intent);
                        break;
                    case 1:
                        String uri1 = "https://www.youtube.com/watch?v=hRtuGEQmm1E";
                        Intent intent1 = new Intent(CardViewActivity.this,WebActivity.class);
                        intent1.putExtra("uri",uri1);
                        startActivity(intent1);
                        break;
                    case 2:
                        String uri2 = "https://www.youtube.com/watch?v=4wjuDG7WXY8";
                        Intent intent2 = new Intent(CardViewActivity.this,WebActivity.class);
                        intent2.putExtra("uri",uri2);
                        startActivity(intent2);
                        break;
                    case 3:
                        String uri3 = "https://www.youtube.com/watch?v=FTBLd2zz8IU";
                        Intent intent3 = new Intent(CardViewActivity.this,WebActivity.class);
                        intent3.putExtra("uri",uri3);
                        startActivity(intent3);
                        break;
                    case 4:
                        String uri4 = "https://www.youtube.com/watch?v=o4vv3PN45Eo";
                        Intent intent4 = new Intent(CardViewActivity.this,WebActivity.class);
                        intent4.putExtra("uri",uri4);
                        startActivity(intent4);
                        break;
                    case 5:
                        String uri5 = "https://www.youtube.com/watch?v=TfT8w5v8KSY";
                        Intent intent5 = new Intent(CardViewActivity.this,WebActivity.class);
                        intent5.putExtra("uri",uri5);
                        startActivity(intent5);
                        break;
                    default:
                        break;

                }
            }

        });


    }
    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:22bb863b-3f88-4322-8cee-9595ce44fc48", // Identity Pool ID
                Regions.AP_NORTHEAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }

    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

    }

    public void setTransferUtility(){
        transferUtility = new TransferUtility(s3, getApplicationContext());
    }

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }



    @Override
    protected void onResume() {
        super.onResume();

    }

    private ArrayList<DataObject> getDataSet() {
        ArrayList results = new ArrayList<DataObject>();

        for (int index = 0; index < moviesImages.length; index++) {
            DataObject obj = new DataObject(moviesNames[index],"Movie ",moviesImages[index]);
            results.add(index, obj);
        }
        return results;
    }

}