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
import android.widget.Button;
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
import java.util.GregorianCalendar;
import java.util.Locale;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.adapter.AndroidDataAdapter;
import in.eloksolutions.ayyappaapp.adapter.AndroidVersion;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.BottomNavigationViewHelper;
import in.eloksolutions.ayyappaapp.helper.GetEventsHome;
import in.eloksolutions.ayyappaapp.helper.GetGroups;
import in.eloksolutions.ayyappaapp.helper.GetSwamiContacts;
import in.eloksolutions.ayyappaapp.listeners.RecyclerItemClickListener;
import in.eloksolutions.ayyappaapp.maps.MapsMarkerActivity;
import in.eloksolutions.ayyappaapp.util.DataObject;
import in.eloksolutions.ayyappaapp.util.Util;


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
    String userId,userName;
    EditText searchValue;
    TextView month, day, date ,rightMonth,rightDay,rightDate ;
    private final String movies[] =  {"Ayyappa janmarahasyam", "Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan", "Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith", "Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas", "Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji", "Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
        // Set up the ViewPager with the sections adapter.
        month=(TextView) findViewById(R.id.month);
        //day=(TextView) findViewById(R.id.day);
        date=(TextView) findViewById(R.id.date);
        rightMonth=(TextView) findViewById(R.id.month_r);
        //rightDay=(TextView) findViewById(R.id.day_r);
        rightDate=(TextView) findViewById(R.id.date_r);
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
        userName=preferences.getString("firstName",null)+", "+preferences.getString("lastName",null);
        searchValue=(EditText) findViewById(R.id.search_name);
        searchValues=searchValue.getText().toString();
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
                deeshaDates();

            } catch (ParseException e) {
                e.printStackTrace();
            }

            topic.setText(diff+" of "+noOfDays+" days");
        }else{
            try {
                datacalling();
            } catch (ParseException e) {
                e.printStackTrace();
            }
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
        TextView noData=(TextView) findViewById(R.id.tv_no_data);
        String url= Config.SERVER_URL+"padipooja/gettoppoojas/"+userId;
        GetEventsHome getEvents=new GetEventsHome(context,url,rvPadi,noData);
        getEvents.execute();

        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups_home);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager groups = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(groups);
        TextView noDatas=(TextView) findViewById(R.id.tv_no_group);
        String gurl= Config.SERVER_URL+"group/getfirstgroups/"+userId;
        GetGroups getGroups=new GetGroups(context,gurl,rvGroups, noDatas);
        System.out.println("url for group list"+gurl);
        getGroups.execute();

        RecyclerView Contacts = (RecyclerView) findViewById(R.id.contacts_home);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager contactLay = new LinearLayoutManager(CardViewActivity.this);
        rvPadi.setLayoutManager(contactLay);
        String curl= Config.SERVER_URL+"user/connections/"+userId;
        GetSwamiContacts getContacts=new GetSwamiContacts(context,curl,rvPadi);
        getContacts.execute();

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
        final Button padiCreate=(Button) findViewById(R.id.padi_create);
        noDatas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent padipooj = new Intent(context,CreateGroup.class);
                startActivity(padipooj);
            }
        });
        noData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent padipooj = new Intent(context,CreatePadiPooja.class);
                startActivity(padipooj);
            }
        });
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
        final Button creategroup=(Button) findViewById(R.id.create_group);
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
                Intent songsIntent = new Intent(context,ContactActivity.class);
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
    public void datacalling() throws ParseException {
        Calendar myCal = new GregorianCalendar();
        month.setText(myCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
        day.setText(myCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        date.setText(""+myCal.get(Calendar.DATE));
        rightMonth.setText(myCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
        rightDay.setText(myCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        rightDate.setText(""+myCal.get(Calendar.DATE));
    }


    public void deeshaDates() throws ParseException {
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        String deekshaStartDate=preferences.getString("startDate",null);
        Date startDate=(new SimpleDateFormat("dd-MM-yyyy")).parse(deekshaStartDate);
        Log.i("CardViewACTIVTY","START DATE "+startDate);
       Calendar myCal = new GregorianCalendar();
        myCal.setTime(startDate);

        month.setText(myCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
       // day.setText(myCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        date.setText(""+myCal.get(Calendar.DATE));

        String eDate=preferences.getString("endDate",null);
        Log.i("CardViewACTIVTY","END DATE "+startDate);
        Date endDate= null;
        try {
            endDate = (new SimpleDateFormat("dd-MM-yyyy")).parse(eDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
         myCal = new GregorianCalendar();
        myCal.setTime(endDate);
        rightMonth.setText(myCal.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()));
        //rightDay.setText(myCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault()));
        rightDate.setText(""+myCal.get(Calendar.DATE));

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
                                String uri = "vxpEMuM1eBc";
                                Intent intent = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent.putExtra("uri",uri);
                                startActivity(intent);
                                break;
                            case 1:

                                String uri1 = "vxpEMuM1eBc";
                                Intent intent1 = new Intent(CardViewActivity.this,WebActivity.class);
                                intent1.putExtra("uri",uri1);
                                startActivity(intent1);
                                break;
                            case 2:
                                String uri2 = "4wjuDG7WXY8";
                                Intent intent2 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent2.putExtra("uri",uri2);
                                startActivity(intent2);
                                break;
                            case 3:
                                String uri3 = "FTBLd2zz8IU";
                                Intent intent3 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent3.putExtra("uri",uri3);
                                startActivity(intent3);
                                break;
                            case 4:

                                String uri4 = "o4vv3PN45Eo";
                                Intent intent4 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent4.putExtra("uri",uri4);
                                startActivity(intent4);
                                break;
                            case 5:

                                String uri5 = "frhBvKlaoLI";
                                Intent intent5 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent5.putExtra("uri",uri5);
                                startActivity(intent5);
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
                                String uri = "ZRYJdPrHiSM";
                                Intent intent = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent.putExtra("uri",uri);
                                startActivity(intent);
                                break;
                            case 1:
                                String uri1 = "zV0lDPtAUxw";
                                Intent intent1 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent1.putExtra("uri",uri1);
                                startActivity(intent1);
                                break;
                            case 2:
                                String uri2 = "nquYSlnavuM";
                                Intent intent2 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent2.putExtra("uri",uri2);
                                startActivity(intent2);
                                break;
                            case 3:
                                String uri3 = "pNGdT5obEys";
                                Intent intent3 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent3.putExtra("uri",uri3);
                                startActivity(intent3);
                                break;
                            case 4:
                                String uri4 = "tzLX8me67wU";
                                Intent intent4 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
                                intent4.putExtra("uri",uri4);
                                startActivity(intent4);
                                break;
                            case 5:
                                String uri5 = "BOjJGALm2kQ";
                                Intent intent5 = new Intent(CardViewActivity.this,PlayYoutubeActivity.class);
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.card_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {

            case R.id.feed:
                Intent feed=new Intent(CardViewActivity.this, FeedBackForm.class);
                startActivity(feed);
                return true;
            case R.id.share:
                startActivity(Util.getInviteIntent(userName));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
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