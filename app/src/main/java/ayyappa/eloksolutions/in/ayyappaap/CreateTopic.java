package ayyappa.eloksolutions.in.ayyappaap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.koushikdutta.urlimageviewhelper.UrlImageViewCallback;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.leocardz.link.preview.library.LinkPreviewCallback;
import com.leocardz.link.preview.library.SourceContent;
import com.leocardz.link.preview.library.TextCrawler;

import java.util.Date;

import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GetShareGroups;


/**
 * Created by welcome on 9/6/2017.
 */

public class CreateTopic extends AppCompatActivity {

    String tag = "Create Topic";
    private TextCrawler textCrawler;
    EditText description,editTextTitlePost, editTextDescriptionPost;
    Context mcontext;
    AmazonS3 s3;
    TransferUtility transferUtility;
    String addTopic;

    private ViewGroup dropPreview;
    private String currentTitle = "", currentUrl, currentCannonicalUrl,
            currentDescription;
    private Bitmap[] currentImageSet;
    private int currentItem = 0;
    String userId,username;
    private Bitmap currentImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_topic);
        Button post = (Button) findViewById(R.id.post);
        description = (EditText) findViewById(R.id.gdescription);
        description.setTypeface(Typeface.DEFAULT);
          textCrawler = new TextCrawler();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        addTopic=description.getText().toString();
        SharedPreferences sharedPreferences=getSharedPreferences(Config.APP_PREFERENCES, MODE_PRIVATE);
        userId=sharedPreferences.getString("userId", null);
        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            } else if (type.startsWith("image/")) {
                handleSendImage(intent); // Handle single image being sent
            }
        } else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                // handleSendMultipleImages(intent); // Handle multiple images being sent
            }
        } else {
            // Handle other intents, such as being started from the home screen

        }
        mcontext=this;

        credentialsProvider();
        // callback method to call the setTransferUtility method
        setTransferUtility();
        RecyclerView rvGroups = (RecyclerView) findViewById(R.id.rv_groups);
        rvGroups.setHasFixedSize(true);
        LinearLayoutManager lmPadi = new LinearLayoutManager(this);
        rvGroups.setLayoutManager(lmPadi);
        String surl= Config.SERVER_URL+"/group/joined/"+userId;
        GetShareGroups getGroups=new GetShareGroups(mcontext,surl,rvGroups,s3,transferUtility,addTopic);
        System.out.println("url for group list"+surl);
        getGroups.execute();


        post.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {
                if ((description.getText().toString() != null && !description.getText().toString().isEmpty())) {
                    Intent post = new Intent(CreateTopic.this, UserGroupsJoined.class);
                    post.putExtra("topicName", description.getText().toString());
                    startActivity(post);
                } else {
                    description.setError("Enter the Description");
                }
            }
        });
    }

    void handleSendText(Intent intent) {
        String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if (sharedText != null) {
            // Update UI to reflect text being shared
            description.setText(sharedText);
            Log.i(tag, "Shared Text is" + sharedText);

            try {
                if (sharedText.contains("http://") || sharedText.contains("https://")) {
                    System.out.println(" in if it contains http...");
                    Log.i(tag,"enter into to the text");
                    textCrawler.makePreview(callback, description.getText().toString());
                    description.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void handleSendImage(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        if (imageUri != null) {
            // Update UI to reflect image being shared
        }
    }

    private LinkPreviewCallback callback = new LinkPreviewCallback() {
        /**
         * This view is used to be updated or added in the layout after getting
         * the result
         */
        private View mainView;
        private LinearLayout linearLayout;
        private View loading;
        private ImageView imageView;

        @Override
        public void onPre() {
            hideSoftKeyboard();

            currentImageSet = null;
            currentItem = 0;

            currentImage = null;
            currentTitle = currentDescription = currentUrl = currentCannonicalUrl = "";

            /** Inflating the preview layout */
            mainView = getLayoutInflater().inflate(R.layout.main_view, null);

            linearLayout = (LinearLayout) mainView.findViewById(R.id.external);

            /**
             * Inflating a loading layout into Main View LinearLayout
             */
            loading = getLayoutInflater().inflate(R.layout.progress,
                    linearLayout);

            dropPreview.addView(mainView);
        }

        @Override
        public void onPos(final SourceContent sourceContent, boolean isNull) {

            /** Removing the loading layout */
            linearLayout.removeAllViews();

            if (!isNull || !sourceContent.getFinalUrl().equals("")) {
                description.setVisibility(View.GONE);
          /*  } else {*/
                currentImageSet = new Bitmap[sourceContent.getImages().size()];

                /**
                 * Inflating the content layout into Main View LinearLayout
                 */
                final View content = getLayoutInflater().inflate(
                        R.layout.preview_content, linearLayout);

                /** Fullfilling the content layout */
                final LinearLayout infoWrap = (LinearLayout) content
                        .findViewById(R.id.info_wrap);
                final LinearLayout titleWrap = (LinearLayout) infoWrap
                        .findViewById(R.id.title_wrap);

                final ImageView imageSet = (ImageView) content
                        .findViewById(R.id.image_post_set);

                final TextView close = (TextView) titleWrap
                        .findViewById(R.id.close);
                final TextView titleTextView = (TextView) titleWrap
                        .findViewById(R.id.title);
                final TextView urlTextView = (TextView) content
                        .findViewById(R.id.url);
                final TextView descriptionTextView = (TextView) content
                        .findViewById(R.id.description);

                titleTextView.setText(TextCrawler.extendedTrim(titleTextView.getText().toString()));
                descriptionTextView.setText(TextCrawler.extendedTrim(descriptionTextView.getText().toString()));

                close.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View arg0) {
                        releasePreviewArea();
                        description.setVisibility(View.VISIBLE);
                    }
                });

                if (sourceContent.getImages().size() > 0) {

                    UrlImageViewHelper.setUrlDrawable(imageSet, sourceContent
                            .getImages().get(0), new UrlImageViewCallback() {

                        @Override
                        public void onLoaded(ImageView imageView,
                                             Bitmap loadedBitmap, String url,
                                             boolean loadedFromCache) {
                            if (loadedBitmap != null) {
                                currentImage = loadedBitmap;
                                System.out.println("current image is :"+currentImage);
                                Uri uri = getImageUri(CreateTopic.this,currentImage);
                                System.out.println("bitmap afer calling the method is uri is  :"+uri);
                                uploadImage(uri);
                                currentImageSet[0] = loadedBitmap;
                            }
                        }
                    });

                } else {
                    showHideImage(imageSet, infoWrap, false);
                }

                titleTextView.setText(sourceContent.getTitle());
                urlTextView.setText(sourceContent.getCannonicalUrl());
                descriptionTextView.setText(sourceContent.getDescription());


            }else {
                description.setVisibility(View.VISIBLE);
            }


            currentTitle = sourceContent.getTitle();
            currentDescription = sourceContent.getDescription();
            currentUrl = sourceContent.getUrl();
            currentCannonicalUrl = sourceContent.getCannonicalUrl();


        }


    };
    private void hideSoftKeyboard() {
        hideSoftKeyboard(description);

        if (editTextTitlePost != null)
            hideSoftKeyboard(editTextTitlePost);
        if (editTextDescriptionPost != null)
            hideSoftKeyboard(editTextDescriptionPost);
    }
    private void hideSoftKeyboard(EditText editText) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager
                .hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }
    private void releasePreviewArea() {
        dropPreview.removeAllViews();
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        System.out.println("bitmap in converting uri methos is :"+inImage);
        // String path = "";

        //ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        // inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    private void uploadImage(Uri resultUri) {
        System.out.println("In image uploading method...");

    }
    private void showHideImage(View image, View parent, boolean show) {
        if (show) {
            image.setVisibility(View.VISIBLE);
            parent.setPadding(5, 5, 5, 5);
            parent.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 2f));
        } else {
            image.setVisibility(View.GONE);
            parent.setPadding(5, 5, 5, 5);
            parent.setLayoutParams(new LinearLayout.LayoutParams(0,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 3f));
        }
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

    public int daysBetween(Date d1, Date d2){
        return (int)( (d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    @Override
    protected void onResume() {
        super.onResume();

    }





}

