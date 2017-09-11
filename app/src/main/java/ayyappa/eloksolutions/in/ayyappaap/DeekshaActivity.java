package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ayyappa.eloksolutions.in.ayyappaap.beans.DeekshaUserTask;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;


/**
 * Created by welcome on 12/15/2016.
 */

    public class DeekshaActivity extends AppCompatActivity implements View.OnClickListener {
    EditText description;
    TextView fdate, txtdate;
    String Tag="deksha rules";

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.shedule);
        Button shedule=(Button) findViewById(R.id.button);
        final TextView edate=(TextView) findViewById(R.id.txtdate);
        final TextView tdate=(TextView) findViewById(R.id.fdate);
        final TextView desc=(TextView) findViewById(R.id.description);

        final Context ctx = this;
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
       final String memId=preferences.getString("userId", null);
        Log.i(Tag,"userid is memId"+memId);
        shedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = ctx.getSharedPreferences(Config.APP_PREFERENCES, ctx.MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("startDate", tdate.getText().toString());
                edit.putString("endDate", edate.getText().toString());
                edit.commit();
                Log.i(Tag,"Start date is"+sharedPreferences.getString("startDate",null));

                String enddate=edate.getText().toString();
                String txdate=tdate.getText().toString();
                String descrip=desc.getText().toString();
                new DeekshaUserTask(ctx,enddate,txdate,descrip,memId).execute();
                Intent shesuleDeeksha = new Intent(ctx, CardViewActivity.class);
                startActivity(shesuleDeeksha);
            }
        });

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


}



