package in.eloksolutions.ayyappaapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.beans.RegisterDTO;
import in.eloksolutions.ayyappaapp.config.Config;
import in.eloksolutions.ayyappaapp.helper.UserUpdateHelper;
import in.eloksolutions.ayyappaapp.recycleviews.CheckInternet;


/**
 * Created by welcome on 6/30/2017.
 */

public class UserUpdate extends AppCompatActivity {
    EditText name, description, emailId, password, city, phoneNumber, lastName, area;
    RecyclerView rvPadi;
    Button userUpdate;
    String userId;
    Context context;
    int count;
    String tag="TopicView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_registerform);
        name=(EditText) findViewById(R.id.etName);
        lastName=(EditText) findViewById(R.id.et_laName);
        emailId=(EditText) findViewById(R.id.etEmailid);
        password=(EditText) findViewById(R.id.et_password);
        phoneNumber=(EditText) findViewById(R.id.etPhoneNumber);
        area=(EditText) findViewById(R.id.etLocation);
        city=(EditText) findViewById(R.id.etCity);
        userUpdate=(Button) findViewById(R.id.butRegister);
        context=this;
        userId=getIntent().getStringExtra("userId");
        Log.i(tag, "topicId Update is"+userId);
        final Context ctx = this;

        UserUpdateHelper getUserValue=new UserUpdateHelper(this);
        String surl = Config.SERVER_URL+"user/user/5997eeb6e4b031b734205daa";
        System.out.println("url for group topic view list"+surl);
        try {
            String output=getUserValue.new UserUpdateTask(surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}

        userUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               saveEventToServer();


    }
});


    }

    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from Topic" + result);
        if (result!=null){
            Gson gson = new Gson();
            RegisterDTO fromJsonn = gson.fromJson(result, RegisterDTO.class);
            name.setText(fromJsonn.getFirstName());
            lastName.setText(fromJsonn.getLastName());
            emailId.setText(fromJsonn.getEmail());
            phoneNumber.setText(fromJsonn.getMobile());
            userUpdate.setText("Update Here");


            }
        }

    private String saveEventToServer() {
        RegisterDTO registrationrDTO=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(UserUpdate.this)) {
                UserUpdateHelper createtopicpHelper = new UserUpdateHelper(UserUpdate.this);
                String gurl = Config.SERVER_URL +"user/update";
                try {
                    String gId= createtopicpHelper.new TopicUpdateHere(registrationrDTO, gurl).execute().get();
                    return gId;


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(UserUpdate.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private RegisterDTO buildDTOObject() {
        RegisterDTO regTopicDTO= new RegisterDTO();
        regTopicDTO.setUserId(userId);
        String gname= name.getText().toString();
        regTopicDTO.setFirstName(gname);
        String lName=lastName.getText().toString();
        regTopicDTO.setLastName(lName);
        String rEmail= emailId.getText().toString();
        regTopicDTO.setEmail(rEmail);
        String phone = phoneNumber.getText().toString();
        regTopicDTO.setMobile(phone);
        String rcity=city.getText().toString();
        regTopicDTO.setCity(rcity);
        String are=area.getText().toString();
        regTopicDTO.setArea(are);
        String pass=password.getText().toString();
        regTopicDTO.setPassword(pass);

        return regTopicDTO;
    }
    private boolean checkValidation() {
        boolean ret = true;


        return ret;
    }
    }

