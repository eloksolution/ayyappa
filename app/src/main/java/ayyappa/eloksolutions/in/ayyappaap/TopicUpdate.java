package ayyappa.eloksolutions.in.ayyappaap;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDTO;
import ayyappa.eloksolutions.in.ayyappaap.beans.TopicDissDTO;
import ayyappa.eloksolutions.in.ayyappaap.config.Config;
import ayyappa.eloksolutions.in.ayyappaap.helper.TopicUpdateHelper;
import ayyappa.eloksolutions.in.ayyappaap.util.DisObject;


/**
 * Created by welcome on 6/30/2017.
 */

public class TopicUpdate extends AppCompatActivity {
    ImageView TopicImage,discussionCreate;
    TextView topicName, description, noOfJoins;
    EditText addDisscussion;
    RecyclerView rvPadi;
    Button topicUpdate;

    String topicId;

    Context context;
    int count;
    String tag="TopicView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_update);
        topicName=(TextView) findViewById(R.id.topic_name);
        description=(TextView) findViewById(R.id.topic_descr);
         topicUpdate=(Button) findViewById(R.id.but_topic_update);

        context=this;
        topicId=getIntent().getStringExtra("topicId");
        Log.i(tag, "topicId Update is"+topicId);
        final Context ctx = this;

        TopicUpdateHelper gettopicValue=new TopicUpdateHelper(this);
        String surl = Config.SERVER_URL+"topic/"+topicId;
        System.out.println("url for group topic view list"+surl);
        try {
            String output=gettopicValue.new TopicViewTask(surl).execute().get();
            System.out.println("the output from Topic"+output);
            setValuesToTextFields(output);
        }catch (Exception e){}

        topicUpdate.setOnClickListener(new View.OnClickListener() {
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
            TopicDTO fromJsonn = gson.fromJson(result, TopicDTO.class);
            topicName.setText(fromJsonn.getTopic());
            description.setText(fromJsonn.getDescription());

            System.out.println("object resul myrecycler results list view is " + fromJsonn.getDiscussions());
            if (fromJsonn.getDiscussions()!=null) {
                ArrayList results = new ArrayList<DisObject>();
                for (TopicDissDTO d : fromJsonn.getDiscussions()) {
                    DisObject disObject=new DisObject(d.getUserId(),d.getsPostDate(),d.getDissId(),d.getComment(),R.drawable.ayyappa_logo);
                    results.add(disObject);

                }
                MyRecyclerDisscusion mAdapter = new MyRecyclerDisscusion(results);
                rvPadi.setAdapter(mAdapter);
                System.out.println("object result myrecycler results list view is " + results);
            }
        }
    }
    private String saveEventToServer() {
        TopicDTO discussionDto=buildDTOObject();
        if (checkValidation()) {
            if (CheckInternet.checkInternetConenction(TopicUpdate.this)) {
                TopicUpdateHelper createtopicpHelper = new TopicUpdateHelper(TopicUpdate.this);
                String gurl = Config.SERVER_URL +"topic/update";
                try {
                    String gId= createtopicpHelper.new TopicUpdateHere(discussionDto, gurl).execute().get();
                    return gId;


                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            } else {
                CheckInternet.showAlertDialog(TopicUpdate.this, "No Internet Connection",
                        "You don't have internet connection.");
            }
        }
        return null;
    }
    private TopicDTO buildDTOObject() {
        TopicDTO topicDTO= new TopicDTO();
        String gname= topicName.getText().toString();
        topicDTO.setTopic(gname);
        String tDes=description.getText().toString();
        topicDTO.setDescription(tDes);
        topicDTO.setTopicId(topicId);

        return topicDTO;
    }
    private boolean checkValidation() {
        boolean ret = true;


        return ret;
    }
    }

