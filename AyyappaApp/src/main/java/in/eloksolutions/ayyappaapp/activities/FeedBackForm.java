package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.FeedBackUserTask;


/**
 * Created by welcome on 12/16/2016.
 */

public class FeedBackForm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_feedback);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.black));
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        getSupportActionBar().setTitle("FeedBack Form");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button submit=(Button) findViewById(R.id.butSubmit);
        final Context ctx=this;
        final TextView etName=(TextView) findViewById(R.id.etName);
        final TextView etPhoneNumber=(TextView) findViewById(R.id.etEmailid);
        final TextView etEmailid=(TextView) findViewById(R.id.etPhoneNumber);
        final TextView etcomment=(TextView) findViewById(R.id.etcomment);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=etName.getText().toString();
                String phonenumber=etPhoneNumber.getText().toString();
                String emailid=etEmailid.getText().toString();
                String comments=etcomment.getText().toString();
                new FeedBackUserTask(ctx,name,phonenumber,emailid,comments).execute();

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
                Intent home=new Intent(FeedBackForm.this, CardViewActivity.class);
                startActivity(home);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
