package ayyappa.eloksolutions.in.ayyappaap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.RegisterDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.RegisterHelper;

/**
 * Created by welcome on 6/27/2017.
 */

public class Registartion extends AppCompatActivity {
    EditText name, description, emailId, password, city, phoneNumber, lastName, area;
    ImageView gImage;
    Spinner gCatagery;
    SharedPreferences.Editor edit;
    UesrSession session;
    private ProgressDialog progress;
    String tag="Registarion";
String userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registerform);
              SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(Config.User_ID, MODE_PRIVATE);
              edit=sharedPreferences.edit();
        Button createRegister=(Button) findViewById(R.id.butRegister);

        final Context ctx = this;

        createRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String createGroupHelper=saveEventToServer();
                Intent main = new Intent(ctx, MainActivity.class);
                startActivity(main);
            }
        });



        name=(EditText) findViewById(R.id.etName);
        lastName=(EditText) findViewById(R.id.et_laName);
        emailId=(EditText) findViewById(R.id.etEmailid);
        password=(EditText) findViewById(R.id.et_password);
        phoneNumber=(EditText) findViewById(R.id.etPhoneNumber);
        area=(EditText) findViewById(R.id.etLocation);
        city=(EditText) findViewById(R.id.etCity);


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
        String are=area.getText().toString();
        registerDto.setArea(are);
        String pass=password.getText().toString();
        registerDto.setPassword(pass);


        return registerDto;
    }
    protected void onPostExecute(String result) {
        if(!result.equals("")) {
            Gson gson = new Gson();
            Type type = new TypeToken<RegisterDTO>() {}.getType();
            RegisterDTO fromJson = gson.fromJson(result, type);
            String id = fromJson.getUserId();
            String name = fromJson.getFirstName();
            String pincode = fromJson.getArea();
            edit.putString("memId", id);
            edit.putString("name", name);
            edit.putString("pincode", pincode);
            edit.putString("phone", fromJson.getMobile());
            edit.putString("area",fromJson.getArea());
            edit.putString("city",fromJson.getCity());
            edit.putString("state",fromJson.getState());

            edit.commit();
        }else{
            CheckInternet.showAlertDialog(Registartion.this, "Invalid Details",
                    "Please Enter Correct Details.");
        }
        Log.i(tag, "result is Registartions xxxxx" +result);
        progress.dismiss();
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
