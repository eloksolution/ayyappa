package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.adapter.AndroidDataAdapter;
import in.eloksolutions.ayyappaapp.adapter.AndroidVersion;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.BottomNavigationViewHelper;
import in.eloksolutions.ayyappaapp.helper.GetEventsHome;
import in.eloksolutions.ayyappaapp.helper.GetGroups;
import in.eloksolutions.ayyappaapp.listeners.RecyclerItemClickListener;
import in.eloksolutions.ayyappaapp.maps.MapsMarkerActivity;
import in.eloksolutions.ayyappaapp.util.DataObject;


public class CardViewActivity extends AppCompatActivity {
    ListView lv;
    Context context;
    String TAG="AudioPlayer";
    GridView grid;
    ImageView add;
    public static int [] contactImages ={R.drawable.chat_icon, R.drawable.chat_icon,R.drawable.chat_icon};
    public static String [] contactNames={"Contact1","Contact2","Contact 3"};
    public static int [] songImages ={R.drawable.ayy1,R.drawable.ayy2,R.drawable.ayy3,R.drawable.ayy4,R.drawable.ayy5,R.drawable.ayy};
    public static String [] songNames={"Maladharanam Niyamala Toranam","Harivarasanam Viswamohanam","Baghavan Saranam","Ayyappa new song","Ayyappa song","Ayyappa best song"};
    public static int [] moviesImages ={R.drawable.ayy1,R.drawable.ayy2,R.drawable.ayy3,R.drawable.ayy4,R.drawable.ayy5,R.drawable.ayy};
    public static String [] moviesNames={"Ayyappa Swamy Janma Rahasyam Telugu Movie 2014","Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan","Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith","Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas","Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji","Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};
    public static String [] moviesid={"vxpEMuM1eBc","hRtuGEQmm1E","4wjuDG7WXY8","FTBLd2zz8IU","o4vv3PN45Eo","TfT8w5v8KSY"};
    AmazonS3 s3;
    TransferUtility transferUtility;
    RecyclerView mRecyclerView;
    String searchValues;
    private static String LOG_TAG = "CardViewActivity";
    TextView topic;
    String userId;
    EditText searchValue;
    private final String movies[] =  {"Ayyappa janmarahasyam", "Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan", "Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith", "Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas", "Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji", "Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};

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
                        Intent intent3 = new Intent(CardViewActivity.this, MapsMarkerActivity.class);
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
        initRecyclerViews();
        songRecyclerViews();
        context=this;
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        String deekshaStartDate=preferences.getString("startDate",null);
        String deekshaEndDate=preferences.getString("endDate",null);
        userId=preferences.getString("userId",null);

        searchValue=(EditText) findViewById(R.id.search_name);
        searchValues=searchValue.getText().toString();
        Log.i(TAG,"the search values is"+searchValues);

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
                Date startDate=(new SimpleDateFormat("dd-MM-yyyy")).parse(deekshaStartDate);
                Date endDate=(new SimpleDateFormat("dd-MM-yyyy")).parse(deekshaEndDate);
                Calendar cal=Calendar.getInstance();
                Date today=cal.getTime();
                diff=daysBetween(startDate,today)+1;
                noOfDays=daysBetween(startDate,endDate)+1;
                Log.i(TAG,"Diff date is is "+diff);
            } catch (ParseException e) {
                e.printStackTrace();
            }

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


        RecyclerView rvPadi = (RecyclerView) findViewById(R.id.rvPadi_home);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);
        String url= Config.SERVER_URL+"padipooja/gettoppoojas/"+userId;
        GetEventsHome getEvents=new GetEventsHome(context,url,rvPadi);
        getEvents.execute();

        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups_home);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager groups = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(groups);
        String gurl= Config.SERVER_URL+"group/getfirstgroups/"+userId;
        GetGroups getGroups=new GetGroups(context,gurl,rvGroups);
        System.out.println("url for group list"+gurl);
        getGroups.execute();
        final ImageView noti=(ImageView) findViewById(R.id.contacts_full);
        noti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent padipooj = new Intent(context,SwamiRequest.class);
                startActivity(padipooj);
            }
        });
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
        final ImageView movieFull=(ImageView) findViewById(R.id.movies_full);
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
        final ImageView creategroup=(ImageView) findViewById(R.id.create_group);
        creategroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,CreateGroup.class);
                startActivity(songsIntent);
            }
        });
        final ImageView contacts=(ImageView) findViewById(R.id.contacts_full);
        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,SwamiRequest.class);
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


    }
    private void initRecyclerViews() {

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.movies_recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<AndroidVersion> av = prepareData();
        AndroidDataAdapter movies = new AndroidDataAdapter(getApplicationContext(), av);
        mRecyclerView.setAdapter(movies);
        final Context ctx=this;
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        switch (i) {
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

                        }
                    }
                })
        );

    }
    private void songRecyclerViews() {

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.songs_recycler);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);

        ArrayList<AndroidVersion> av = prepareSongData();
        AndroidDataAdapter movies = new AndroidDataAdapter(getApplicationContext(), av);
        mRecyclerView.setAdapter(movies);
        final Context ctx=this;
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getApplicationContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int i) {
                        switch (i) {
                            case 0:
                                String uri = "https://www.youtube.com/watch?v=ZRYJdPrHiSM";
                                Intent intent = new Intent(CardViewActivity.this,WebActivity.class);
                                intent.putExtra("uri",uri);
                                startActivity(intent);
                                break;
                            case 1:
                                String uri1 = "https://www.youtube.com/watch?v=zV0lDPtAUxw";
                                Intent intent1 = new Intent(CardViewActivity.this,WebActivity.class);
                                intent1.putExtra("uri",uri1);
                                startActivity(intent1);
                                break;
                            case 2:
                                String uri2 = "https://www.youtube.com/watch?v=nquYSlnavuM";
                                Intent intent2 = new Intent(CardViewActivity.this,WebActivity.class);
                                intent2.putExtra("uri",uri2);
                                startActivity(intent2);
                                break;
                            case 3:
                                String uri3 = "https://www.youtube.com/watch?v=pNGdT5obEys";
                                Intent intent3 = new Intent(CardViewActivity.this,WebActivity.class);
                                intent3.putExtra("uri",uri3);
                                startActivity(intent3);
                                break;
                            case 4:
                                String uri4 = "https://www.youtube.com/watch?v=tzLX8me67wU";
                                Intent intent4 = new Intent(CardViewActivity.this,WebActivity.class);
                                intent4.putExtra("uri",uri4);
                                startActivity(intent4);
                                break;
                            case 5:
                                String uri5 = "https://www.youtube.com/watch?v=BOjJGALm2kQ";
                                Intent intent5 = new Intent(CardViewActivity.this,WebActivity.class);
                                intent5.putExtra("uri",uri5);
                                startActivity(intent5);
                                break;

                        }
                    }
                })
        );

    }
    private ArrayList<AndroidVersion> prepareData() {


        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < movies.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(movies[i]);
            mAndroidVersion.setrecyclerViewImage(moviesImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }
    private ArrayList<AndroidVersion> prepareSongData() {


        ArrayList<AndroidVersion> av = new ArrayList<>();
        for (int i = 0; i < movies.length; i++) {
            AndroidVersion mAndroidVersion = new AndroidVersion();
            mAndroidVersion.setAndroidVersionName(songNames[i]);
            mAndroidVersion.setrecyclerViewImage(songImages[i]);
            av.add(mAndroidVersion);
        }
        return av;
    }

    public void searchMethod(View view){
        Intent topicUp = new Intent(this, SearchSwamiRequest.class);
        topicUp.putExtra("searchValue",""+searchValue.getText().toString());
        Log.i(TAG," The Search value is :: "+searchValue.getText().toString());
        startActivity(topicUp);

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
    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

}