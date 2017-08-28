package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import ayyappa.eloksolutions.in.ayyappaap.beans.EventDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.EventMembers;
import ayyappa.eloksolutions.in.ayyappaap.helper.EventViewHelper;
import ayyappa.eloksolutions.in.ayyappaap.helper.PadiObject;
import ayyappa.eloksolutions.in.ayyappaap.util.Constants;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;


public class PadiPoojaView extends AppCompatActivity implements View.OnClickListener {


    ayyappa.eloksolutions.in.ayyappaap.ExpandableListView listview;
    TextView description, event_name,eventdate,eventtime, location, no_of_mem, join_status, text1,tvname;
    ImageButton show, hide;
    ImageView edit,delete;
    ImageView padiImage;
    ImageButton btnInvite;
    Button leavebtn,upDate, joinbtn;
    CardView card_view;
    String padiPoojaId,REG_TOKEN,ownerId, memId, name,sharesms,eventnamesms;
    String tag="PadiPoojaView";
    int count;
    ArrayList<HashMap<String, String>> oslist = new ArrayList<>();
    RelativeLayout   Relative1;
    Context ctx;
    RecyclerView rvPadi;
    File fileToDownload ;
    AmazonS3 s3;
    EventDTO eventDTO;
    TransferUtility transferUtility;
    TransferObserver transferObserver;
    SharedPreferences preference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.activity_padi_pooja_view);
        SharedPreferences preferences = getSharedPreferences(Config.userId, Context.MODE_PRIVATE);
        REG_TOKEN=preferences.getString("TOKEN", null);
        System.out.println("Registration token is "+REG_TOKEN);
        memId = preferences.getString("memId", null);
        name = preferences.getString("name", null);
        System.out.println("result from eventview" + 19);
        ctx=getApplicationContext();

        Intent createpadipoojaintent=getIntent();
        padiPoojaId=createpadipoojaintent.getStringExtra("padiPoojaId");
        Log.i(tag,"padiPoojaId"+padiPoojaId);
        //  Relative1=(RelativeLayout)findViewById(R.id.Relative1);
        event_name = (TextView)findViewById(R.id.event_name);
        eventdate = (TextView)findViewById(R.id.event_date);
        eventtime = (TextView)findViewById(R.id.event_timee);
        location = (TextView)findViewById(R.id.location);
        description = (TextView)findViewById(R.id.description);
        padiImage=(ImageView) findViewById(R.id.padi_image_view);
        upDate=(Button) findViewById(R.id.update);
        no_of_mem = (TextView)findViewById(R.id.joins_view);
      //  tvname = (TextView)findViewById(R.id.hostmember);
        card_view = (CardView)findViewById(R.id.card_view);
        text1 = (TextView)findViewById(R.id.text1);
        //join_status = (TextView)findViewById(R.id.join_status);
        //  leavebtn = (Button) findViewById(R.id.leavebtn);
      //  btnInvite = (ImageButton) findViewById(R.id.invitebtn);
     //   edit = (ImageView)findViewById(R.id.edit);
        // delete = (ImageView)findViewById(R.id.delete);
      //  show = (ImageButton) findViewById(R.id.show);
//        btnInvite.setOnClickListener(this);
        joinbtn=(Button) findViewById(R.id.joinbtn);
        joinbtn.setOnClickListener(this);
        upDate.setOnClickListener(this);
        preference=getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);

        // hide = (ImageButton) findViewById(R.id.hide);
       // hide.setOnClickListener(this);
       // listview = (ayyappa.eloksolutions.in.ayyappaap.ExpandableListView) findViewById(R.id.listview);
        try {

            File outdirectory=this.getCacheDir();
            fileToDownload=File.createTempFile("GRO","jpg",outdirectory);

        }catch (Exception e){
            e.printStackTrace();
        }
        credentialsProvider();
        setTransferUtility();

        EventViewHelper eventViewHelper = new EventViewHelper(this);
        String surl = Config.SERVER_URL + "padipooja/padipoojaEdit/"+padiPoojaId;
        try {
            String output=eventViewHelper.new Eventview(surl).execute().get();
            System.out.println("the output from PadiPooja"+output);
            setValuesToTextFields(output);
            System.out.println("groupDTO.getImagePath()"+eventDTO.getImagePath());
            setFileToDownload("padipooja/P_535_1503928966841");
        }catch (Exception e){}

        rvPadi = (RecyclerView) findViewById(R.id.rv_members);
        rvPadi.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvPadi.setLayoutManager(lmPadi);


    }

    public void credentialsProvider(){

        // Initialize the Amazon Cognito credentials provider
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),
                "ap-northeast-1:22bb863b-3f88-4322-8cee-9595ce44fc48", // Identity Pool ID
                Regions.AP_NORTHEAST_1 // Region
        );

        setAmazonS3Client(credentialsProvider);
    }
    public void transferObserverListener(TransferObserver transferObserver){

        Bitmap bit= BitmapFactory.decodeFile(fileToDownload.getAbsolutePath());
        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.i("File down load status ", state+"");
                Log.i("File down load id", id+"");
                if("COMPLETED".equals(state.toString())){
                    Bitmap bit= BitmapFactory.decodeFile(fileToDownload.getAbsolutePath());
                    padiImage.setImageBitmap(bit);
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                int percentage = (int) (bytesCurrent/bytesTotal * 100);
                Log.e("percentage",percentage +"");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e("error","error");
            }


        });
    }
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

    }

    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }
    public void setFileToDownload(String imageKey){
        if (Util.isEmpty(imageKey))return;
       /* try {
            S3Object s3Object=s3.getObject("elokayyappa",imageKey);
            InputStream reader = s3Object.getObjectContent();
            FileOutputStream fos=(new FileOutputStream(fileToDownload));
            byte[] buffer=new byte[4096];
            int read=0;
            while ((read=reader.read(buffer))!=-1) {
                fos.write(buffer,0,read);
            }
            fos.flush();
            fos.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/



        transferObserver = transferUtility.download(
                "elokayyappa",     // The bucket to download from *//*
                imageKey,    // The key for the object to download *//*
                fileToDownload        // The file to download the object to *//*
        );

        transferObserverListener(transferObserver);

    }



    @Override
    public void onClick(View v) {
        Log.i(tag,"Onclick for join"+v.getId());
        if (v == edit) {
            Intent intent = new Intent(getBaseContext(), PadiPoojaFull.class);
            intent.putExtra("padiPoojaId", padiPoojaId);
            startActivity(intent);
        }
        if (v == delete) {
            deleteEvent();
        }
        if (v == btnInvite) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBodyText =sharesms+"--Sent from AyyappApp";
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT,eventnamesms);
            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBodyText);
            startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));


        }
        if (v == show) {
            System.out.println("Show button");
            show.setVisibility(View.INVISIBLE);
            hide.setVisibility(View.VISIBLE);
            description.setMaxLines(Integer.MAX_VALUE);
        }
        if (v == hide) {
            System.out.println("Hide button");
            hide.setVisibility(View.INVISIBLE);
            show.setVisibility(View.VISIBLE);
            description.setMaxLines(2);
        }
        if (v == joinbtn) {

            joinbtn.setVisibility(View.GONE);
            joinEvent();

        }
        if (v == leavebtn) {
            leaveEvent();
        }
        if (v == upDate) {
            Intent padiUdate=new Intent(ctx, PadiPoojaUpdate.class);
            padiUdate.putExtra("padiPoojaId", ""+padiPoojaId);
          startActivity(padiUdate);
        }
    }

    private void leaveEvent() {
        EventViewHelper eventViewHelper = new EventViewHelper(this);
        String surl = Config.SERVER_URL + "padipooja/padipoojaEdit/" + padiPoojaId + "/" + memId;
        try {
            String output=eventViewHelper.new LeaveEvent(surl).execute().get();
            System.out.println("the output from editevent"+output);
            //leaveEvent(output);
        }catch (Exception e){}
              /*  new LeaveEvent().execute();*/
        String topic= Constants.EVENT_TOPIC+padiPoojaId;
        Intent i= new Intent(ctx, RegistrationIntentService.class);
        ctx.startService(i);
    }

    private void joinEvent() {
        EventMembers eventMembers = buildDTOObject();
        EventViewHelper eventViewHelper = new EventViewHelper(this);
        String surl = Config.SERVER_URL + "padipooja/join";
        try {
            String joinmem=eventViewHelper.new JoinEvent(eventMembers,surl).execute().get();
            System.out.println("the output from JoinEvent"+joinmem);
            addingMember(joinmem);
        }catch (Exception e){}
    }

    private void deleteEvent() {
        new AlertDialog.Builder(this)
                .setTitle("Are you sure!")
                .setMessage("if you click on ok this event will be deleted permanently.")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        EventViewHelper eventViewHelper = new EventViewHelper(null);
                        String surl = Config.SERVER_URL + "event/delete/" + padiPoojaId;
                        eventViewHelper.new DeleteEvent(surl).execute();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.drawable.alert)
                .show();
    }

    public void setValuesToTextFields(String result) {
        System.out.println("json from eventview" + result);
        if (result!=null){
            Gson gson = new Gson();
             eventDTO = gson.fromJson(result, EventDTO.class);
            event_name.setText(eventDTO.getEventName());
            eventnamesms=eventDTO.getEventName();
            eventdate.setText(eventDTO.getDate());
            eventtime.setText(eventDTO.gettime());
            System.out.println("past from eventfromJson.gettime()" + eventDTO.gettime());

//           tvname.setText(fromJson.getOwnerName());
      //     sharesms=name+", is inviting you to join padi pooja on "+fromJson.getDate()+" time"+fromJson.gettime()+" at"+fromJson.getLocation();
            System.out.println("past from event view" + eventDTO.getPast());

            if (eventDTO.getPadiMembers() != null)
                no_of_mem.setText(eventDTO.getPadiMembers().size() + " members are going");
            else
                no_of_mem.setText("No Members Joined Yet");
            eventdate.setText(eventDTO.getdate()+"");
            location.setText(eventDTO.getLocation());
            description.setText(eventDTO.getDescription());
            if (String.valueOf(eventDTO.getPadipoojaId()).equals(memId)){
                Log.i(tag,"memberid and owner"+memId);
                Relative1.setVisibility(View.GONE);
                edit.setVisibility(View.VISIBLE);
                delete.setVisibility(View.VISIBLE);
            }
            String topic= Constants.EVENT_TOPIC+padiPoojaId;
            if (eventDTO.getMember()) {
                btnInvite.setVisibility(View.VISIBLE);
                joinbtn.setVisibility(View.GONE);
                try {
                    Intent i= new Intent(ctx, RegistrationIntentService.class);
                    i.putExtra("TOPIC", topic);
                    startService(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                join_status.setText("You already Joined");
            }else {
                try {
                    Intent i= new Intent(ctx, RegistrationIntentService.class);
                    i.putExtra("TOPIC", topic);
                    i.putExtra("evesub", true);
                    startService(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (eventDTO.getPadiMembers()!=null) {
                ArrayList results = new ArrayList<PadiObject>();
                for (RegisterDTO m : eventDTO.getPadiMembers()) {

                    PadiObject mem = new PadiObject(m.getUserId(), m.getFirstName(), R.drawable.ayyappa_logo, m.getLastName());
                    results.add(mem);

                }
                MyRecyclerPadiMembers mAdapter = new MyRecyclerPadiMembers(results);
                rvPadi.setAdapter(mAdapter);
                System.out.println("object resul myrecycler results list view is " + results);
            }

        }
    }
    private void addingMember(String result) throws JSONException {
        JSONObject jsonObject;
        jsonObject = new JSONObject(result);
        btnInvite.setVisibility(View.VISIBLE);
        joinbtn.setVisibility(View.GONE);
        join_status.setText("You already Joined");
        count=count+1;
        no_of_mem.setText(count + " members are going");
        HashMap<String, String> map = new HashMap<>();
        map.put("Name", jsonObject.getString("memberName"));
        oslist.add(map);
        CustomAdapter adapter = new CustomAdapter(this, oslist,
                R.layout.activity_padi_pooja_list_view,
                new String[]{"Name"}, new int[]{R.id.member_namee});
        listview.setAdapter(adapter);
        listview.setFocusable(false);
    }

    private EventMembers buildDTOObject() {
        EventMembers eventMembers = new EventMembers();
        eventMembers.setPadiId(padiPoojaId);
        eventMembers.setUserId("596c75afa4ff23ccc3e363e0");
        eventMembers.setFirstName("suresh");
        eventMembers.setLastName("Babu");
        eventMembers.setPadiName(eventnamesms);

        return eventMembers;
    }
    private void leaveEvent(String result) {
        System.out.println(result);
        Toast.makeText(getBaseContext(), "You left from " + event_name.getText() + " event", Toast.LENGTH_LONG).show();
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("eventName", event_name.getText());
            System.out.println(event_name);
            joinbtn.setVisibility(View.VISIBLE);
            leavebtn.setVisibility(View.GONE);
            card_view.setVisibility(View.GONE);
            join_status.setText("You have not joined this event");
            no_of_mem.setText((count-1) + " members are going");
            HashMap<String, String> map = new HashMap<>();
            String Name=name;
            map.put("Name", Name);
            map.remove(Name);
            oslist.remove(map);
            CustomAdapter adapter = new CustomAdapter(this, oslist,
                    R.layout.activity_padi_pooja_list_view,
                    new String[]{name}, new int[]{R.id.member_namee});
            listview.setAdapter(adapter);
            listview.setFocusable(false);
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), " Unable to leave event please contact support", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Necessary to restore the BottomBar's state, otherwise we would
        // lose the current tab on orientation change.
    }


}




