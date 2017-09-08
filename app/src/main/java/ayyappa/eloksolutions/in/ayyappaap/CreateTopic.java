package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by welcome on 9/6/2017.
 */

public class CreateTopic extends AppCompatActivity {
    EditText description;
    String tag="Create Topic";
   // private TextCrawler textCrawler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_topic);
        Button post=(Button) findViewById(R.id.post);
      description=(EditText) findViewById(R.id.gdescription);

      //  textCrawler = new TextCrawler();
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
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



      post.setOnClickListener(new View.OnClickListener() {
          @Override

          public void onClick(View view) {
              if((description.getText().toString()!= null && !description.getText().toString().isEmpty())) {
                  Intent post = new Intent(CreateTopic.this, GroupList.class);
                  post.putExtra("topicName", description.getText().toString());
                  startActivity(post);
              }else {
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
            Log.i(tag,"Shared Text is"+sharedText);
            try {
                if(description.getText().toString().contains("http://") || description.getText().toString().contains("https://")){
                    System.out.println(" in if it contains http...");
                   // textCrawler.makePreview(callback, description.getText().toString());
                    //description.setVisibility(View.GONE);
                }
            }catch (Exception e){
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

    }
