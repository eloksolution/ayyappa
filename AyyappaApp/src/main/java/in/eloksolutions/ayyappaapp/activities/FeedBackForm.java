package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.FeedBackUserTask;


/**
 * Created by welcome on 12/16/2016.
 */

public class FeedBackForm extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_feedback);
        Button submit=(Button) findViewById(R.id.butSubmit);
        final Context ctx=this;
        final TextView etName=(TextView) findViewById(R.id.etName);
        final TextView etPhoneNumber=(TextView) findViewById(R.id.etEmailid);
        final TextView etEmailid=(TextView) findViewById(R.id.etPhoneNumber);
        final TextView etcomment=(TextView) findViewById(R.id.etcomment);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name=etName.getText().toString();
                String phonenumber=etPhoneNumber.getText().toString();
                String emailid=etEmailid.getText().toString();
                String comments=etcomment.getText().toString();
                new FeedBackUserTask(ctx,name,phonenumber,emailid,comments).execute();

            }
        });


    }

}
