package in.eloksolutions.ayyappaapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.recycleviews.ServicesGridView;

public class PlayYoutubeActivity extends YouTubeBaseActivity {
    GridView grid;
    public static final String YT_API_KEY = "ENTER_YOUR_KEY_HERE";
    String video_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_youtube);

        YouTubePlayerView youTubePlayerView =
                (YouTubePlayerView) findViewById(R.id.player);
        video_code=getIntent().getStringExtra("uri");
        youTubePlayerView.initialize(YT_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {

                        // do any work here to cue video, play video, etc.
                            youTubePlayer.cueVideo(video_code);
                        // or to play immediately
                         youTubePlayer.loadVideo(video_code);
                    }
                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(PlayYoutubeActivity.this, "Youtube Failed!", Toast.LENGTH_SHORT).show();
                    }
                });
        ImageView home=(ImageView) findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent groupCreate= new Intent(PlayYoutubeActivity.this, CardViewActivity.class);
                startActivity(groupCreate);
            }
        });
        final String[] services = new String[] {"Ayyappa janmarahasyam", "Ayyappa Swamy Mahatyam Full Movie | Sarath Babu | Silk Smitha | K Vasu | KV Mahadevan", "Ayyappa Telugu Full Movie Exclusive - Sai Kiran, Deekshith", "Ayyappa Swamy Mahatyam | Full Length Telugu Movie | Sarath Babu, Shanmukha Srinivas", "Ayyappa Deeksha Telugu Full Movie | Suman, Shivaji", "Ayyappa Swamy Janma Rahasyam Telugu Full Movie"};
        String [] Images={
                "vxpEMuM1eBc",
                "hRtuGEQmm1E",
                "4wjuDG7WXY8",
                "FTBLd2zz8IU",
                "o4vv3PN45Eo",
                "TfT8w5v8KSY"

        };

        ServicesGridView adapter = new ServicesGridView(PlayYoutubeActivity.this, services, Images);
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
                        Intent intent = new Intent(PlayYoutubeActivity.this,PlayYoutubeActivity.class);
                        intent.putExtra("uri",uri);
                        startActivity(intent);
                        break;
                    case 1:
                        String uri1 = "hRtuGEQmm1E";
                        Intent intent1 = new Intent(PlayYoutubeActivity.this,PlayYoutubeActivity.class);
                        intent1.putExtra("uri",uri1);
                        startActivity(intent1);
                        break;
                    case 2:
                        String uri2 = "4wjuDG7WXY8";
                        Intent intent2 = new Intent(PlayYoutubeActivity.this,PlayYoutubeActivity.class);
                        intent2.putExtra("uri",uri2);
                        startActivity(intent2);
                        break;
                    case 3:
                        String uri3 = "FTBLd2zz8IU";
                        Intent intent3 = new Intent(PlayYoutubeActivity.this,PlayYoutubeActivity.class);
                        intent3.putExtra("uri",uri3);
                        startActivity(intent3);
                        break;
                    case 4:
                        String uri4 = "o4vv3PN45Eo";
                        Intent intent4 = new Intent(PlayYoutubeActivity.this,PlayYoutubeActivity.class);
                        intent4.putExtra("uri",uri4);
                        startActivity(intent4);
                        break;
                    case 5:
                        String uri5 = "TfT8w5v8KSY";
                        Intent intent5 = new Intent(PlayYoutubeActivity.this,PlayYoutubeActivity.class);
                        intent5.putExtra("uri",uri5);
                        startActivity(intent5);
                        break;
                    default:
                        break;

                }
            }

        });

    }

}
