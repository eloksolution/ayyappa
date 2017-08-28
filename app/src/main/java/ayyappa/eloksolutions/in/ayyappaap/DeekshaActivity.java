package ayyappa.eloksolutions.in.ayyappaap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;


/**
 * Created by welcome on 12/15/2016.
 */




    public class DeekshaActivity extends AppCompatActivity  {
    EditText description;
    TextView fdate, txtdate;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.padicreate);
       /* ImageView shedule=(ImageView) findViewById(R.id.button);
        final TextView edate=(TextView) findViewById(R.id.txtdate);
        final TextView tdate=(TextView) findViewById(R.id.fdate);
        final TextView desc=(TextView) findViewById(R.id.description);

        final Context ctx = this;
        SharedPreferences preferences = getSharedPreferences(Config.userId, MODE_PRIVATE);
       final String memId=preferences.getString("memId", null);
        shedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enddate=edate.getText().toString();
                String txdate=tdate.getText().toString();
                String descrip=desc.getText().toString();
                new DeekshaUserTask(ctx,enddate,txdate,descrip,memId).execute();
                Intent shesuleDeeksha = new Intent(ctx, PadiPoojaView.class);
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
*/

    }


}



