package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.GroupDTO;
import ayyappa.eloksolutions.in.ayyappaap.helper.GroupHelper;

/**
 * Created by welcome on 6/27/2017.
 */

public class CreateGroup extends AppCompatActivity {
    EditText name, description;
    ImageView gImage;
    Spinner gCatagery;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_group);


        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Create Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        String[] catageries = new String[]{"Select Group catagory","Padipooja","Pilgrimage","AyyappaSwami Temples","Ayyappa Stories","Bhakthi News"};
        gCatagery = (Spinner) findViewById(R.id.gcatagery);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, catageries);
        gCatagery.setAdapter(adapter);

        Button createGroup=(Button) findViewById(R.id.butgcreate);

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

        name=(EditText) findViewById(R.id.gname);
        description=(EditText) findViewById(R.id.gdescription);
        gCatagery=(Spinner) findViewById(R.id.gcatagery);

    }

    private String saveEventToServer() {
        GroupDTO groupDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(CreateGroup.this)) {
                GroupHelper createGroupHelper = new GroupHelper(CreateGroup.this);
                String gurl = "http://192.168.0.2:8080/AyyappaService/group/add";
                try {
                    String gId= createGroupHelper.new CreateGroup(groupDto, gurl).execute().get();
                    return gId;

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(CreateGroup.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private GroupDTO buildDTOObject() {
     GroupDTO groupDto=new GroupDTO();

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
