package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetEventsHome;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetGroups;
import ayyappa.eloksolutions.in.ayyappaap.util.DataObject;


public class CardViewActivity extends AppCompatActivity {

    private SectionsPageAdapter mSectionsPageAdapter;

    private ViewPager mViewPager;

    ListView lv;
    Context context;
    MyRecyclerViewAdapter mAdapter;
    ArrayList prgmName;
    MediaPlayer mediaPlayer;
    String TAG="AudioPlayer";
    int currentTrack = 0;
    private int [] songs ={R.raw.song1,R.raw.song2,R.raw.song3};
    boolean isPlayOrPause=true;

    public static int [] contactImages ={R.drawable.chat_icon,R.drawable.chat_icon,R.drawable.chat_icon};
    public static String [] contactNames={"Contact1","Contact2","Contact 3"};

    public static int [] songImages ={R.drawable.ayy1,R.drawable.ayy2,R.drawable.ayy3,R.drawable.ayy4,R.drawable.ayy5,R.drawable.ayy};
    public static String [] songNames={"Maladharanam Niyamala Toranam","Harivarasanam Viswamohanam","Baghavan Saranam","Ayyappa4","Ayyappa5","Ayyappa6"};
    public static int [] moviesImages ={R.drawable.ayy1,R.drawable.ayy2,R.drawable.ayy3,R.drawable.ayy4,R.drawable.ayy5,R.drawable.ayy};
    public static String [] moviesNames={"Ayyappa Swamy Janma Rahasyam Telugu Movie 2014","Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan","Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith","Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas","Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji","Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};
    public static String [] moviesid={"vxpEMuM1eBc","hRtuGEQmm1E","4wjuDG7WXY8","FTBLd2zz8IU","o4vv3PN45Eo","TfT8w5v8KSY"};

    RecyclerView mRecyclerView;
    private static String LOG_TAG = "CardViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);
       /* ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_action_name);
        // Enabling Up / Back navigation
        actionBar.show();*/

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
                        Intent intent3 = new Intent(CardViewActivity.this, MapsActivity.class);
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
        context=this;
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        String deekshaStartDate=preferences.getString("startDate",null);
        String deekshaEndDate=preferences.getString("endDate",null);

       /* final ScrollView scrollView=(ScrollView) findViewById(R.id.scrollView);
        scrollView.smoothScrollTo(0, 0);
        scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
            @Override
            public void onGlobalLayout() {
                scrollView.fullScroll(View.FOCUS_UP);
            }
        });*/

    /*    lv=(ListView) findViewById(R.id.lvSongs);
        lv.setAdapter(new CustomAdapter(context, songNames, moviesImages));

        lv=(ListView) findViewById(R.id.lvMovies);
        lv.setAdapter(new CustomAdapter(context, moviesNames, moviesImages));
    */

        final ImageView imgDeeksha=(ImageView) findViewById(R.id.imgDeeksha);
        final TextView tvDays=(TextView) findViewById(R.id.tvDays);
        Log.i(TAG,"Deeksha Config.getUserId()"+preferences.getString("userId", null));
        Log.i(TAG,"Deeksha Config.getUserName()"+preferences.getString("lastName", null));

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

            imgDeeksha.setVisibility(View.GONE);
            tvDays.setText(diff+"th Day of "+noOfDays+" days");
        }else{
            tvDays.setText("");
            imgDeeksha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(context, DeekshaActivity.class));
                }
            });

        }

        songPlayerSetup();

       /* final ImageView createPadipooja=(ImageView) findViewById(R.id.imgCreatePadi);
        createPadipooja.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CreatePadiPooja.class));
            }
        });

        final ImageView imgInvite=(ImageView) findViewById(R.id.imgInvite);
        imgInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "You are invited to experience Ayyappa Swamy app.");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });
*/
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
        final ImageView ivFull=(ImageView) findViewById(R.id.ivFull);
        ivFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //    Intent songsIntent = new Intent(context,SongsActivity.class);
               // startActivity(songsIntent);
            }
        });
        final ImageView moviFull=(ImageView) findViewById(R.id.PadiFull);
        moviFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent padipooj = new Intent(context,PadiPoojaFull.class);
                startActivity(padipooj);
            }
        });
        final ImageView movieFull=(ImageView) findViewById(R.id.ivFull11);
        movieFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,Movis.class);
                startActivity(songsIntent);
            }
        });
        final ImageView contactFull=(ImageView) findViewById(R.id.contactFull);
        contactFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent songsIntent = new Intent(context,GroupList.class);
                startActivity(songsIntent);
            }
        });
    }


    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }

    private void songPlayerSetup() {
        MediaPlayer.OnCompletionListener mediaListener = getOnCompletionListener();
        mediaPlayer = MediaPlayer.create(this, R.raw.song1);
        mediaPlayer.setOnCompletionListener(mediaListener);
        long finalTime = mediaPlayer.getDuration();

        final TextView tvSongName = (TextView) this.findViewById(R.id.tvSongName);

        final ImageView play = (ImageView) findViewById(R.id.ibplay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isPlayOrPause) {
                    mediaPlayer.start();
                    int timeElapsed = mediaPlayer.getCurrentPosition();
                    Log.i(TAG, "Time elapsed " + timeElapsed);
                    play.setImageResource(R.drawable.pause1);
                    isPlayOrPause=false;
                }else {
                    mediaPlayer.pause();
                    int timeElapsed = mediaPlayer.getCurrentPosition();
                    Log.i(TAG, "Time elapsed " + timeElapsed);
                    play.setImageResource(R.drawable.play);
                    isPlayOrPause=true;
                }
            }
        });

        ImageView next = (ImageView) findViewById(R.id.ibnext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaPlayer.release();
                    currentTrack++;
                    currentTrack=currentTrack%(songs.length);
                    Log.i(TAG,"CURRENT Track "+currentTrack);
                    setupMediaPlayer(tvSongName,songs[currentTrack],songNames[currentTrack]);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });


        ImageView prev = (ImageView) findViewById(R.id.ibprev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaPlayer.release();;
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    currentTrack--;
                    if(currentTrack<0)currentTrack=songs.length-1;
                    currentTrack=currentTrack%(songs.length);
                    Log.i(TAG,"CURRENT Track "+currentTrack);
                    mediaPlayer.setDataSource(context, Uri.parse("android.resource://"+ context.getPackageName() + "/raw/"+songs[currentTrack]));
                    tvSongName.setText(songNames[currentTrack]);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setupMediaPlayer(TextView tvSongName,int songResourceId,String songName) throws IOException {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setDataSource(context, Uri.parse("android.resource://"+ context.getPackageName() + "/raw/"+songResourceId));
        tvSongName.setText(songName);
        mediaPlayer.prepare();
        mediaPlayer.start();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_feedback:
                startActivity(new Intent(this, FeedBackForm.class));
                return true;
            case R.id.action_home:
                startActivity(new Intent(this, CardViewActivity.class));
               // startActivity(new Intent(this, CarousalActivity.class));
                return true;
            case R.id.action_rules:
                startActivity(new Intent(this, DeekshaRules.class));
                return true;
            case R.id.contactus:
                startActivity(new Intent(this, ContactUs.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @NonNull
    private MediaPlayer.OnCompletionListener getOnCompletionListener() {

        return new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer arg0) {
                arg0.release();
                currentTrack++;
                currentTrack=currentTrack%(songs.length);
                Log.i(TAG,"CURRENT Track "+currentTrack);
                arg0 = MediaPlayer.create(getApplicationContext(), songs[currentTrack]);
                arg0.setOnCompletionListener(this);
                arg0.start();
            }
        };
    }
}