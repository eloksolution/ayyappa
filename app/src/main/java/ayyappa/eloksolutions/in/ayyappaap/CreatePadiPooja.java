package ayyappa.eloksolutions.in.ayyappaap;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.EventDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.CreatePadiPoojaHelper;


public class CreatePadiPooja extends AppCompatActivity implements View.OnClickListener {
    EditText event_name, location, description;
    TextView date,time;
    String memid,name;

    private static final String TAG = "CreatePadiPooja";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_create_padipooja);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create PadiPooja");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button createhere=(Button) findViewById(R.id.butCreateHere);

        final Context ctx = this;
        createhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String eventid=saveEventToServer();

            }
        });

        // Initializes TransferUtility, always do this before using it.

        event_name = (EditText) findViewById(R.id.event_name);
        date = (TextView) findViewById(R.id.fdate);
        date.setText("" + DateFormat.format("dd/MM/yyyy", System.currentTimeMillis()));
        time = (TextView) findViewById(R.id.fromTime);
        time.setText("" + DateFormat.format("hh:mm a", System.currentTimeMillis()));
        location = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        SharedPreferences preferences = getSharedPreferences(Config.User_ID, Context.MODE_PRIVATE);
         memid = preferences.getString("memid", "");
         name = preferences.getString("name", "");
        date.setOnClickListener(this);
        time.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        if (v == date) {
            DateAndTimePicker.datePickerDialog(this, date);
        }
        if (v == time) {
            showToTimePicker();
        }

    }
    private void showToTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = DateAndTimePicker.getTimePickerDialog(hour, minute, this, time);
        mTimePicker.show();
    }

    private String saveEventToServer() {
       EventDTO eventDTO = buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(CreatePadiPooja.this)) {
                CreatePadiPoojaHelper createEventHelper = new CreatePadiPoojaHelper(CreatePadiPooja.this);
                String surl = Config.SERVER_URL + "padipooja/add";
                try {
                    String eventid= createEventHelper.new CreateEvent(eventDTO, surl).execute().get();
                    return eventid;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(CreatePadiPooja.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }


    private EventDTO buildDTOObject() {
        EventDTO eventDTO = new EventDTO();
        String eventname = event_name.getText().toString();
        eventDTO.setEventName(eventname);
        String s1=date.getText().toString();
        eventDTO.setdate(s1);
        String s=time.getText().toString();
        System.out.println(s);
        eventDTO.setTime(s);
        String loc = location.getText().toString();
        eventDTO.setLocation(loc);
        String desc = description.getText().toString();
        eventDTO.setDescription(desc);
        eventDTO.setOwner(memid);
        eventDTO.setOwnerName(name);
        return eventDTO;
    }
    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(description)) ret = false;
        if (!Validation.hasText(location)) ret = false;
        if (!Validation.hasText(event_name)) ret = false;
        return ret;
    }
       }



