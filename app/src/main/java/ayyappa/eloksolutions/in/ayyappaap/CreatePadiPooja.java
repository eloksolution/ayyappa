package ayyappa.eloksolutions.in.ayyappaap;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.EventDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.CreatePadiPoojaHelper;
import ayyappa.eloksolutions.in.ayyappaap.util.Util;


public class CreatePadiPooja extends AppCompatActivity implements View.OnClickListener {
    EditText event_name, location, description;
    TextView date,time;
    String memid,name;
    private int STORAGE_PERMISSION_CODE = 23;
    File fileToDownload = new File("/storage/sdcard0/Pictures/MY");
    AmazonS3 s3;
    private Button buttonRequestPermission;
    TransferUtility transferUtility;

    String keyName;
    File fileToUpload;

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

        buttonRequestPermission = (Button) findViewById(R.id.buttonRequestPermission);

        buttonRequestPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //First checking if the app is already having the permission
                if(isReadStorageAllowed()){
                    //If permission is already having then showing the toast
                    Toast.makeText(CreatePadiPooja.this,"You already have the permission",Toast.LENGTH_LONG).show();
                    //Existing the method with return
                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();
            }
        });
        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

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
    public void setFileToUpload(View view){

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= 19) {
            // For Android versions of KitKat or later, we use a
            // different intent to ensure
            // we can get the file path from the returned intent URI
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }

        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }

    private void showToTimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = DateAndTimePicker.getTimePickerDialog(hour, minute, this, time);
        mTimePicker.show();
    }
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
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
    public void setAmazonS3Client(CognitoCachingCredentialsProvider credentialsProvider){

        // Create an S3 client
        s3 = new AmazonS3Client(credentialsProvider);

        // Set the region of your S3 bucket
        s3.setRegion(Region.getRegion(Regions.US_EAST_1));

    }

    public void setTransferUtility(){

        transferUtility = new TransferUtility(s3, getApplicationContext());
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            try {
                Uri uri = data.getData();
                String path = getPath(getApplicationContext(), uri);
                Toast.makeText(this, "File path is " + path, Toast.LENGTH_LONG).show();
                Log.e(TAG, "File path is " + path);
                fileToUpload = new File(path);


                System.out.println("the uri is" + uri);

            } catch (Exception e) {
                Toast.makeText(this, "Unable to get the file from the given URI.  See error log for details" + e.getMessage(),
                        Toast.LENGTH_LONG).show();
                Log.e(TAG, "Unable to upload file from the given uri", e);
            }
        }
    }
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());

    }
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    private String saveEventToServer() {
        keyName="padipooja/P_"+ Util.getRandomNumbers()+"_"+System.currentTimeMillis();
       EventDTO eventDTO = buildDTOObject();
        TransferObserver transferObserver = transferUtility.upload(
                "elokayyappa",     /* The bucket to upload to */
                keyName,    /* The key for the uploaded object */
                fileToUpload       /* The file where the data to upload exists */
        );
        transferObserverListener(transferObserver);
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
    public void transferObserverListener(TransferObserver transferObserver){

        transferObserver.setTransferListener(new TransferListener(){

            @Override
            public void onStateChanged(int id, TransferState state) {
                Log.e("statechange", state+"");
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

    private EventDTO buildDTOObject() {
      SharedPreferences  preference=getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
       String userId=preference.getString("userId",null);
        String firstName=preference.getString("firstName",null);
        String lastName=preference.getString("lastName",null);

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
        eventDTO.setOwner(userId);
        eventDTO.setImagePath(keyName);
        Log.i(TAG,"Key name of the  images"+keyName);
        eventDTO.setOwnerName(firstName+lastName);
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



