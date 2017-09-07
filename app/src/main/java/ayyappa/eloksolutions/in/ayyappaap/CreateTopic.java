package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by welcome on 9/6/2017.
 */

public class CreateTopic extends AppCompatActivity {
    EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_topic);
        Button post=(Button) findViewById(R.id.post);
      description=(EditText) findViewById(R.id.gdescription);

      post.setOnClickListener(new View.OnClickListener() {
          @Override

          public void onClick(View view) {
              if((description.getText().toString()!= null && !description.getText().toString().isEmpty())) {
                  Intent post = new Intent(CreateTopic.this, ToppicGroupList.class);
                  post.putExtra("topicName", description.getText().toString());
                  startActivity(post);
              }else {
                  description.setError("Enter the Description");
              }
          }
      });
    }
}
