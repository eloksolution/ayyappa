package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private BottomBar bottomBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                if (tabId == R.id.tab_calls) {
                    Intent i = new Intent(MainActivity.this, GroupList.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_groups) {

                    Intent i = new Intent(MainActivity.this, TopicFull.class);
                    startActivity(i);
                } else if (tabId == R.id.tab_chats) {
                    Intent i = new Intent(MainActivity.this, CreateGroup.class);
                    startActivity(i);

                }
             else if (tabId == R.id.tab_home) {

                Intent i = new Intent(MainActivity.this, PadiPoojaFull.class);
                startActivity(i);
            } else if (tabId == R.id.tab_profile) {
                    Intent regiser=new Intent(MainActivity.this, Registartion.class);
                    startActivity(regiser);


                }
            }
        });
    }
}