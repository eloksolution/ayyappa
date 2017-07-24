package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.GroupDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.GroupUpdateHelper;

/**
 * Created by welcome on 6/27/2017.
 */

public class GroupUpdate extends AppCompatActivity {
    EditText name, description;
    ImageView gImage;
    Spinner gCatagery;
    String groupId,tag="GroupUpDate";
    Button createGroup;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);

        groupId=getIntent().getStringExtra("groupId");
        Log.i(tag, "groupId for GroupUpdate is"+groupId);

        String[] catageries = new String[]{"Select Group catagory","Padipooja","Pilgrimage","AyyappaSwami Temples","Ayyappa Stories","Bhakthi News"};
        gCatagery = (Spinner) findViewById(R.id.gcatagery);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item, catageries);
        gCatagery.setAdapter(adapter);
        name=(EditText) findViewById(R.id.gname);
        description=(EditText) findViewById(R.id.gdescription);
        gCatagery=(Spinner) findViewById(R.id.gcatagery);

         createGroup=(Button) findViewById(R.id.butgcreate);

        Button imagePick=(Button) findViewById(R.id.group_image_add);
        final Context ctx = this;

        createGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String createGroupHelper=saveEventToServer();

            }
        });

        imagePick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                            + "/myFolder/");
                    intent.setDataAndType(uri, "text/csv");
                    startActivity(Intent.createChooser(intent, "Open folder"));

            }
        });
        GroupUpdateHelper getGroupsValue=new GroupUpdateHelper(this);
        String surl = Config.SERVER_URL+"group/groupEdit/"+groupId;
        System.out.println("url for group list"+surl);
        try {
            String output=getGroupsValue.new GroupUpdateTask(surl).execute().get();
            System.out.println("the output from Group"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}

    }
    public void setValuesToTextFields(String result) {
        System.out.println("json xxxx from groupview" + result);
        if (result != null) {
            Gson gson = new Gson();
            GroupDTO fromJsonn = gson.fromJson(result, GroupDTO.class);
            name.setText(fromJsonn.getName());
            description.setText(fromJsonn.getDescription());
            createGroup.setText("Update Here");

        }
    }
    private String saveEventToServer() {
        GroupDTO groupDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(GroupUpdate.this)) {
                GroupUpdateHelper createGroupHelper = new GroupUpdateHelper(GroupUpdate.this);
                String gurl = "http://192.168.0.2:8080/AyyappaService/group/update";
                try {
                    String gId= createGroupHelper.new GroupUpdateHere(groupDto, gurl).execute().get();
                    return gId;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(GroupUpdate.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private GroupDTO buildDTOObject() {
     GroupDTO groupDto=new GroupDTO();
        groupDto.setGroupid(groupId);
        String gname= name.getText().toString();
        groupDto.setName(gname);
        String gdesc= description.getText().toString();
        groupDto.setDescription(gdesc);
        String groupCatagery = gCatagery.getSelectedItem().toString();
        groupDto.setGroupCatagory(groupCatagery);

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
