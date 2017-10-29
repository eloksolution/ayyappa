package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.config.Config;


/**
 * Created by welcome on 12/15/2016.
 */

    public class DeekshaActivity extends AppCompatActivity implements View.OnClickListener {
    EditText description;
    TextView fdate, txtdate;
    String Tag="deksha rules";
    String tDate,eDate;
    Context ctx;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.shedule);
        final TextView edate=(TextView) findViewById(R.id.txtdate);
        final TextView tdate=(TextView) findViewById(R.id.fdate);
        final TextView desc=(TextView) findViewById(R.id.description);
      Toolbar  toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Start Deeksha");
        toolbar.setTitle("Start Deeksha");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Context ctx = this;
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
       final String memId=preferences.getString("userId", null);
        Log.i(Tag,"userid is memId"+memId);
        tDate=tdate.getText().toString();
        eDate=edate.getText().toString();
        fdate = (TextView) findViewById(R.id.fdate);
            fdate.setText("" + DateFormat.format(" dd-MM-yyyy", System.currentTimeMillis()));
        txtdate = (TextView) findViewById(R.id.txtdate);
           txtdate.setText("" + DateFormat.format(" dd-MM-yyyy", System.currentTimeMillis()));
        description = (EditText) findViewById(R.id.description);

        fdate.setOnClickListener(this);
        txtdate.setOnClickListener(this);
        description.setOnClickListener(this);


}
    public void onClick(View v) {

        if (v == fdate) {
            DateAndTimePicker.datePickerDialog(this, fdate);

        }
        if (v == txtdate) {
            DateAndTimePicker.datePickerDialog(this, txtdate);
        }


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                this.onBackPressed();
                return true;

            case R.id.action_settings:
                SharedPreferences sharedPreferences = ctx.getSharedPreferences(Config.APP_PREFERENCES, ctx.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("startDate", tDate);
                edit.putString("endDate", eDate);
                edit.commit();
                Log.i(Tag,"Start date is"+sharedPreferences.getString("startDate",null));

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}



