package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by welcome on 1/9/2017.
 */

public class ContactUs extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactus);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.action_feedback:
                startActivity(new Intent(this, FeedBackForm.class));
                return true;
            case R.id.action_home:
                startActivity(new Intent(this, CardViewActivity.class));
                // startActivity(new Intent(this, CarousalActivity.class));
                return true;
            case R.id.action_rules:
                startActivity(new Intent(this, DeekshaRules.class));
                return true;
            case R.id.contactus:
                startActivity(new Intent(this, ContactUs.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
