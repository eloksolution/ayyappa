package in.eloksolutions.ayyappaapp.activities;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.EventDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.PadiUpdateHelper;
import in.eloksolutions.ayyappaapp.recycleviews.CheckInternet;
import in.eloksolutions.ayyappaapp.recycleviews.Validation;
import in.eloksolutions.ayyappaapp.util.Util;


public class PadiPoojaUpdate extends AppCompatActivity implements View.OnClickListener {


    TextView date, time, pin_my_location, contact_number,addImage;
    CheckBox eventList;
    EditText addresss, event_name, description, city, email, phone;
    ImageView imgView;

    String imageName, UserId, userName;
    int pLACE_PICKER_REQUEST=1;

    String memid,name,padiPoojaId,tag="PadiUdate";
    Button createhere;
    private int STORAGE_PERMISSION_CODE = 23;
    File fileToDownload = new File("/storage/sdcard0/Pictures/MY");
    AmazonS3 s3;
    private Button buttonRequestPermission;
    TransferUtility transferUtility;
    EventDTO eventDTO;
    String keyName;
    Button create;
    File fileToUpload;

    private static final String TAG = "CreatePadiPooja";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from new_activity.xml
        setContentView(R.layout.create_event);
        padiPoojaId =getIntent().getStringExtra("padiPoojaId");
        Log.i(tag, "padiupdate Id id "+padiPoojaId);
        final Context ctx = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Padipooja Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Initializes TransferUtility, always do this before using it.
        event_name = (EditText) findViewById(R.id.topic_title);
        description = (EditText) findViewById(R.id.discription);
        addImage=(TextView) findViewById(R.id.bg_image);
        date = (TextView) findViewById(R.id.Date);
        // date.setText("" + DateFormat.format("dd/MM/yyyy", System.currentTimeMillis()));
        time = (TextView) findViewById(R.id.time1);
        //time.setText("" + DateFormat.format("hh:mm a", System.currentTimeMillis()));
        pin_my_location = (TextView) findViewById(R.id.pin_my_location);
        addresss = (EditText) findViewById(R.id.Adress);
        city=(EditText) findViewById(R.id.city);
        imgView=(ImageView) findViewById(R.id.groups_image);
        SharedPreferences preferences = getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
        UserId = preferences.getString("userId", "");
        Log.i(tag,"the user Id in SharedPreferances :: "+UserId);
        userName = preferences.getString("firstName", "") + " " + preferences.getString("secoundName", "");
        date.setOnClickListener(this);
        time.setOnClickListener(this);

        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();
        PadiUpdateHelper getGroupsValue=new PadiUpdateHelper(this);
        String surl = Config.SERVER_URL+"padipooja/padipoojaEdit/"+padiPoojaId+"/"+UserId;
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new PadiUpdateTask(surl).execute().get();
            System.out.println("the output from Group"+output);
            setValuesToTextFields(output);

        }catch (Exception e){
            e.printStackTrace();
        }

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

    public void setFileToUpload(View view){
        if(isReadStorageAllowed()) {

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
        }else{
            requestStoragePermission();
        }
        credentialsProvider();

        setTransferUtility();
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

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                CropImageView.CropShape cropShape = CropImageView.CropShape.RECTANGLE;
                Uri uri = data.getData();
                CropImage.activity(uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        // .setFixAspectRatio(true)
                        .setCropShape(cropShape)
                        // .setAspectRatio(4,2)
                        .setMinCropResultSize(480,720)
                        .setMaxCropResultSize(800,1200)
                        .start(this);

                System.out.println("the uri is" + uri);

            } catch (Exception e) {

                Log.e(TAG, "Unable to upload file from the given uri", e);
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                imgView.setVisibility(View.VISIBLE);
                imgView.setScaleType(ImageView.ScaleType.FIT_XY);
                imgView.setImageURI(resultUri);
                String path = getPath(getApplicationContext(), resultUri);
                fileToUpload = new File(path);
                Log.e(TAG, "File path is " + path);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
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
    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

  /*  public void callBackUpdateUI(String padiPoojaId){
        Intent padiView=new Intent(this, PadiPoojaView.class);
        padiView.putExtra("padipoojaId", padiPoojaId);
        startActivity(padiView);

    }*/
    private String saveEventToServer() {
        keyName = "padipooja/P_" + Util.getRandomNumbers() + "_" + System.currentTimeMillis();
        EventDTO eventDTO = buildDTOObject();
        Log.i(TAG,"Padipooja Images File"+fileToUpload);
        TransferObserver transferObserver = transferUtility.upload(
                "elokayyappa",     /* The bucket to upload to */
                keyName,    /* The key for the uploaded object */
                fileToUpload       /* The file where the data to upload exists */
        );

        transferObserverListener(transferObserver);

        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(PadiPoojaUpdate.this)) {
                PadiUpdateHelper createEventHelper = new PadiUpdateHelper(PadiPoojaUpdate.this);
                String surl = Config.SERVER_URL + "padipooja/update";

                try {
                    String gId= createEventHelper.new PadiUpdateHere(eventDTO, surl).execute().get();
                    return gId;

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
    public void setValuesToTextFields(String result) {
        System.out.println("json from eventview" + result);
        if (result != null) {
            Gson gson = new Gson();
            eventDTO = gson.fromJson(result, EventDTO.class);
            event_name.setText(eventDTO.getEventName());
            date.setText(eventDTO.getDate());
            time.setText(eventDTO.gettime());
            description.setText(eventDTO.getDescription());
            addresss.setText(eventDTO.getLocation());
            city.setText(eventDTO.getCity());
            System.out.println("past from event getmems view" + eventDTO.getPadiMembers());
        }
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
        String loc = addresss.getText().toString();
        eventDTO.setLocation(loc);
        String desc = description.getText().toString();
        eventDTO.setDescription(desc);
        eventDTO.setOwner(memid);
        eventDTO.setOwnerName(name);
        eventDTO.setImagePath(keyName);
        Log.i(tag,"event Dato at  padipooja"+eventDTO);
        return eventDTO;

    }


    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(description)) ret = false;
        if (!Validation.hasText(addresss)) ret = false;
        if (!Validation.hasText(event_name)) ret = false;
        return ret;
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
            case R.id.feed:
                Intent feed=new Intent(PadiPoojaUpdate.this, FeedBackForm.class);
                startActivity(feed);
                return true;
            case R.id.share:
                startActivity(Util.getInviteIntent(userName));
                return true;
            case R.id.action_settings:

                if (checkValidation () ) {
                    if (CheckInternet.checkInternetConenction(PadiPoojaUpdate.this)) {
                        String createGroupHelper=saveEventToServer();
                    }else {
                        CheckInternet.showAlertDialog(PadiPoojaUpdate.this, "No Internet Connection",
                                "You don't have internet connection.");
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
       }



