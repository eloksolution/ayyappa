package in.eloksolutions.ayyappaapp.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.util.Message;
import in.eloksolutions.ayyappaapp.util.URLUtil;

public class ChatActivity extends AppCompatActivity {
    private List<Message> listMessages;
    private ChatArrayAdapter chatArrayAdapter;
    private EditText chatmsg;
    public static String TAG="ChatActivity.class";
    Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        Log.i(TAG,"Starting Chat version X0003 ......");
        ListView listView = (ListView) findViewById(R.id.lvChat);
        listMessages=new ArrayList<>();
        chatArrayAdapter = new ChatArrayAdapter(this, listMessages,"memId");
        listView.setAdapter(chatArrayAdapter);
        System.out.println("before click");
        ImageView sendmsg=(ImageView) findViewById(R.id.btSend);
        chatmsg=(EditText) findViewById(R.id.etMessage);
        ctx=this;

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatmsg.getText() != null && chatmsg.getText().toString().trim().length() > 0) {
                    System.out.println("Button is clicked sending message");
                    Date d = new Date();
                    String msg=chatmsg.getText().toString();
                    Message m = new Message("1", msg, d, "", "memId", "name");
                    listMessages.add(m);
                    chatArrayAdapter.notifyDataSetChanged();
                    chatmsg.setText("");
                    InputStream in=null;
                    try {
                        in=ctx.getAssets().open("fcm.google.com.crt");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    AsyncTaskRunner runner = new AsyncTaskRunner(in,msg);
                    //String msg = chatmsg.getText().toString();
                    Log.i(TAG,"Message is "+msg);
                    runner.execute(msg);

                }

            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(message,
                new IntentFilter("in.eloksolutions.ayyappa.SEND_MSG"));
    }

    private BroadcastReceiver message = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("message");
            Log.d(TAG, "Received message "+msg);
            Date d = new Date();
            Message m = new Message("1", msg, d, "", "memId", "name");
            listMessages.add(m);
            chatArrayAdapter.notifyDataSetChanged();
        }
    };

    private class AsyncTaskRunner extends AsyncTask<String, String, String> {
        InputStream in;
        private String resp;
        String msg;
        AsyncTaskRunner(InputStream in,String msg){
            this.in=in;
            this.msg=msg;
        }
        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping..."); // Calls onProgressUpdate()
                // Do your long operations here and return the result

            Log.i(TAG,"Message in async task is "+msg);
            String result="Server did not responded";
            try {
                result= URLUtil.post(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Sleeping for given time period

            return resp;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            //finalResult.setText(result);
            chatmsg.setText("");
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            // Things to be done before execution of long running operation. For
            // example showing ProgessDialog
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onProgressUpdate(Progress[])
         */
        @Override
        protected void onProgressUpdate(String... text) {
            chatmsg.setText("Sending...");
            // Things to be done while execution of long running operation is in
            // progress. For example updating ProgessDialog
        }
    }
}
