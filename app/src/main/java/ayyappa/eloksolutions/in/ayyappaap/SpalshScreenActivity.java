package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;

/**
 * Created by welcome on 7/14/2017.
 */

public class SpalshScreenActivity extends AppCompatActivity {
    String id,otp;
    Intent mainIntent;
    Context ctx=this;
    private static int SPLASH_TIME_OUT = 3000;
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreenmaker);
        StartAnimations();
    }
    private void StartAnimations() {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.logo);
        iv.clearAnimation();
        iv.startAnimation(anim);
        SharedPreferences preferences=getSharedPreferences(Config.User_ID, Context.MODE_PRIVATE);
        id=preferences.getString("memId","");
        otp=preferences.getString("otp","");
        System.out.println("id in splash"+id+"and otp"+otp);
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                if(id!=null && id.length()>0){
                    mainIntent = new Intent(SpalshScreenActivity.this, MainActivity.class);
                }
                else {
                    mainIntent = new Intent(SpalshScreenActivity.this, MainActivity.class);
                }
                // This method will be executed once the timer is over
                // Start your app main activity
                SpalshScreenActivity.this.startActivity(mainIntent);
                SpalshScreenActivity.this.finish();
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}