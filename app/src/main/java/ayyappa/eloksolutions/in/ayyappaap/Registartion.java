package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.RegisterHelper;

/**
 * Created by welcome on 6/27/2017.
 */

public class Registartion extends AppCompatActivity {
    EditText name, description, emailId, password, city, phoneNumber, lastName;
    ImageView gImage;
    Spinner gCatagery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registerform);

        Button createRegister=(Button) findViewById(R.id.butRegister);

        Button imagePick=(Button) findViewById(R.id.group_image_add);
        final Context ctx = this;

        createRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String createGroupHelper=saveEventToServer();
                Intent main = new Intent(ctx, MainActivity.class);
                startActivity(main);
            }
        });

        imagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.drawable.ayy1);

                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/png");
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share via"));

            }
        });

        name=(EditText) findViewById(R.id.etname);
        lastName=(EditText) findViewById(R.id.etname);
        emailId=(EditText) findViewById(R.id.email);
        password=(EditText) findViewById(R.id.password);
        phoneNumber=(EditText) findViewById(R.id.mobile);
        city=(EditText) findViewById(R.id.location);


    }

    private String saveEventToServer() {
        RegisterDTO registerDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(Registartion.this)) {
                RegisterHelper createRegisterHelper = new RegisterHelper(Registartion.this);
                String gurl = Config.SERVER_URL+"user/add";
                try {
                    String gId= createRegisterHelper.new CreateRegistration(registerDto, gurl).execute().get();
                    return gId;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(Registartion.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private RegisterDTO buildDTOObject() {
     RegisterDTO registerDto=new RegisterDTO();

        String rname= name.getText().toString();
        registerDto.setFirstName(rname);
        String rlname= lastName.getText().toString();
        registerDto.setLastName(rlname);
        String rEmail= emailId.getText().toString();
        registerDto.setEmail(rEmail);
        String phone = phoneNumber.getText().toString();
        registerDto.setMobile(phone);
        String rcity=city.getText().toString();
        registerDto.setCity(rcity);
        String pass=password.getText().toString();
        registerDto.setPassword(pass);


        return registerDto;
    }

    private boolean checkValidation() {
        boolean ret = true;
        if (!Validation.hasText(name)) ret = false;

        if (!Validation.hasText(lastName)) ret = false;
        return ret;



        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        return true;

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {

            case R.id.exit:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
