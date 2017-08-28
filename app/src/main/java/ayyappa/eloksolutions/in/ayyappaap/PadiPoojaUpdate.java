package ayyappa.eloksolutions.in.ayyappaap;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.EventDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.PadiUpdateHelper;


public class PadiPoojaUpdate extends AppCompatActivity implements View.OnClickListener {
    EditText event_name, location, description;
    TextView date,time;
    String memid,name,padiPoojaId,tag="PadiUdate";
    Button createhere;

    private static final String TAG = "CreatePadiPooja";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_create_padipooja);

         createhere=(Button) findViewById(R.id.butCreateHere);
        padiPoojaId =getIntent().getStringExtra("padiPoojaId");
        Log.i(tag, "padiupdate Id id "+padiPoojaId);
        final Context ctx = this;
        createhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              saveEventToServer();

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
        SharedPreferences preferences = getSharedPreferences(Config.userId, Context.MODE_PRIVATE);
         memid = preferences.getString("memid", "");
         name = preferences.getString("name", "");
        date.setOnClickListener(this);
        time.setOnClickListener(this);

        PadiUpdateHelper getGroupsValue=new PadiUpdateHelper(this);
        String surl = Config.SERVER_URL+"padipooja/padipoojaEdit/"+padiPoojaId;
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new PadiUpdateTask(surl).execute().get();
            System.out.println("the output from Group"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}

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
    public void setValuesToTextFields(String result) {
        System.out.println("json from eventview" + result);
        if (result != null) {
            Gson gson = new Gson();
            EventDTO fromJson = gson.fromJson(result, EventDTO.class);
            event_name.setText(fromJson.getEventName());
            date.setText(fromJson.getDate());
            time.setText(fromJson.gettime());
            description.setText(fromJson.getDescription());
            createhere.setText("Update Here");

            System.out.println("past from event getmems view" + fromJson.getPadiMembers());
        }
    }

    private String saveEventToServer() {
       EventDTO eventDTO = buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(PadiPoojaUpdate.this)) {
                PadiUpdateHelper createEventHelper = new PadiUpdateHelper(PadiPoojaUpdate.this);
                String surl = Config.SERVER_URL + "padipooja/update";
                try {
                    String eventid= createEventHelper.new GroupUpdateHere(eventDTO, surl).execute().get();
                    return eventid;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(PadiPoojaUpdate.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }



    private EventDTO buildDTOObject() {
        EventDTO eventDTO = new EventDTO();
        eventDTO.setPadipoojaId(padiPoojaId);
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



