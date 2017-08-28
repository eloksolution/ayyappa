package ayyappa.eloksolutions.in.ayyappaap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Locale;
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

    UesrSession session;
    double latti,longi;
    private ProgressDialog progress;
    String tag="Registarion";
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String userId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registerform);

        Button createRegister=(Button) findViewById(R.id.butRegister);

        final Context ctx = this;

        name=(EditText) findViewById(R.id.etName);
        lastName=(EditText) findViewById(R.id.et_laName);
        emailId=(EditText) findViewById(R.id.etEmailid);
        password=(EditText) findViewById(R.id.et_password);
        phoneNumber=(EditText) findViewById(R.id.etPhoneNumber);
        area=(EditText) findViewById(R.id.etLocation);
        city=(EditText) findViewById(R.id.etCity);
        createRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(tag, "id is the sharepreferance"+name.getText().toString());
                String createGroupHelper=saveEventToServer();
                Intent main = new Intent(ctx, MainActivity.class);
                startActivity(main);
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
    }

     void getLocation() {
         if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                 != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                 (this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

             ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

         } else {
             Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

             if (location != null) {
                 latti = location.getLatitude();
                  longi = location.getLongitude();

                 ((TextView) findViewById(R.id.longitude)).
                         setText("Current Location is :" + latti + "," + longi);
                 Geocoder gc= new Geocoder(this, Locale.getDefault());
                 // TextView addr = (TextView) main.findViewById(R.id.editText2);
                 String result="x03";
                 try {
                     List<Address> addressList = gc.getFromLocation(latti,
                             longi, 1);

                     if (addressList != null && addressList.size() > 0) {
                         Address address = addressList.get(0);
                         StringBuilder sb = new StringBuilder();
                         for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                             sb.append(address.getAddressLine(i)).append("\n");
                         }
                         sb.append(address.getLocality()).append("\n");
                         sb.append(address.getPostalCode()).append("\n");
                         sb.append(address.getCountryName());
                         result = sb.toString();
                         area.setText(result);

                     }else {
                         ((TextView) findViewById(R.id.textView)).
                                 setText("Unable to find current location . Try again later");
                     }


                 } catch (Exception e) {
                     e.printStackTrace();
                 }
                 // addr.setText("Address is"+result);
             }else{
                 //  text.setText("Unabletofind");
                 System.out.println("Unable");
             }
             }

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
        registerDto.setLongi(longi);
        registerDto.setLati(latti);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_LOCATION:
                getLocation();
                break;
        }
    }
}
