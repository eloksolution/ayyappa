package in.eloksolutions.ayyappaapp.activities;

import android.Manifest;
import android.app.Activity;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.util.concurrent.ExecutionException;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.GroupDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.GroupHelper;
import in.eloksolutions.ayyappaapp.recycleviews.CheckInternet;
import in.eloksolutions.ayyappaapp.recycleviews.Validation;
import in.eloksolutions.ayyappaapp.util.Util;

/**
 * Created by welcome on 6/27/2017.
 */

public class CreateGroup extends AppCompatActivity {
    EditText name, description;
    ImageView gImage, imgView;
    Spinner gCatagery;
    private static int PICK_FROM_GALLERY;
    private int STORAGE_PERMISSION_CODE = 23;
    File fileToDownload = new File("/storage/sdcard0/Pictures/MY");
    AmazonS3 s3;
    private Button buttonRequestPermission;
    TransferUtility transferUtility;
    String TAG="Create Group";
    String keyName,userId,userName;
    File fileToUpload;
    Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_new_group);

        String[] catageries = new String[]{"Select Group catagory","Padipooja","Pilgrimage","AyyappaSwami Temples","Ayyappa Stories","Bhakthi News"};
        gCatagery = (Spinner) findViewById(R.id.gcatagery);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, catageries);
        gCatagery.setAdapter(adapter);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create New Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imgView=(ImageView) findViewById(R.id.img_view);
        Button imagePick=(Button) findViewById(R.id.group_image_add);
        SharedPreferences preference=getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        userId=preference.getString("userId",null);
        userName=preference.getString("firstName",null)+ " " + preference.getString("lastName", null);
        final Context ctx = this;


        // callback method to call credentialsProvider method.
        credentialsProvider();

        // callback method to call the setTransferUtility method
        setTransferUtility();

        name=(EditText) findViewById(R.id.gname);
        description=(EditText) findViewById(R.id.gdescription);
        gCatagery=(Spinner) findViewById(R.id.gcatagery);

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
    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

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

    private String saveEventToServer() {
        keyName="groups/G_"+ Util.getRandomNumbers()+"_"+System.currentTimeMillis();
        GroupDTO groupDto=buildDTOObject();
        if(fileToDownload!=null) {
            TransferObserver transferObserver = transferUtility.upload(
                    "elokayyappa",     /* The bucket to upload to */
                    keyName,    /* The key for the uploaded object */
                    fileToUpload       /* The file where the data to upload exists */
            );

            transferObserverListener(transferObserver);
        }
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(CreateGroup.this)) {
                GroupHelper createGroupHelper = new GroupHelper(CreateGroup.this);
                String gurl = Config.SERVER_URL+"group/add";
                try {
                    String gId= createGroupHelper.new CreateGroup(groupDto, gurl).execute().get();
                    return gId;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(CreateGroup.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private GroupDTO buildDTOObject() {
        SharedPreferences preference=getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
     GroupDTO groupDto=new GroupDTO();
        String gname= name.getText().toString();
        groupDto.setName(gname);
        String gdesc= description.getText().toString();
        groupDto.setDescription(gdesc);
        String groupCatagery = gCatagery.getSelectedItem().toString();
        groupDto.setGroupCatagory(groupCatagery);
        groupDto.setOwner(userId);
        groupDto.setOwnerName(userName);
        Log.i(TAG,"Config.getUserId()"+preference.getString("userId",null));
        groupDto.setImagePath(keyName);
        return groupDto;
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(description)) ret = false;

        if (!Validation.hasText(name)) ret = false;
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
            case R.id.action_settings:

                if (checkValidation () ) {
                    if (CheckInternet.checkInternetConenction(CreateGroup.this)) {
                        String createGroupHelper=saveEventToServer();
                    }else {
                        CheckInternet.showAlertDialog(CreateGroup.this, "No Internet Connection",
                                "You don't have internet connection.");
                    }
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
