package in.eloksolutions.ayyappaapp.messaging;

/**
 * Created by Vishnu on 9/24/2017.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import in.eloksolutions.ayyappaapp.R;
import in.eloksolutions.ayyappaapp.activities.CardViewActivity;
import in.eloksolutions.ayyappaapp.activities.GroupView;
import in.eloksolutions.ayyappaapp.activities.PadiPoojaView;
import in.eloksolutions.ayyappaapp.activities.TopicView;
import in.eloksolutions.ayyappaapp.activities.UserView;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        String groupId=remoteMessage.getFrom();
        if(groupId!=null && groupId.length()>8){
            groupId=groupId.substring(8);
            System.out.println("Group id id"+groupId);
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Map<String,String> map=remoteMessage.getData();
           getPendingIntent( map);
            return;
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            sendNotification(getHomePendingIntent(),"Ayyappa App","New Message");
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }

    private void getPendingIntent(Map<String, String> map) {
        if(map==null || map.size()==0){
            sendNotification(getHomePendingIntent(),"Ayyappa Messging","New Message");
            return;
        }
        if(map.containsKey("userId")){
            sendNotification(getUserIntent(map.get("userId")),map.get("name"),"New Swami Joined");
            return;
        }
        if(map.containsKey("topicId")){
            sendNotification(getTopicIntent(map.get("topicId")),map.get("name"),"New Ayyappa Topic Added");
            return;
        }
        if(map.containsKey("groupId")){
            sendNotification(getGroupIntent(map.get("groupId")),map.get("name"),"New Group Created");
            return;
        }
        if(map.containsKey("padiId")){
            sendNotification(getGroupIntent(map.get("padiId")),map.get("name"),"New Padi Pooja Created");
            return;
        }
        sendNotification(getHomePendingIntent(),"Ayyappa Messging","New Message");;
    }
    // [END receive_message]


    /**
     * Handle time allotted to BroadcastReceivers.
     */
    private void handleNow() {
        Log.d(TAG, "Short lived task is done.");
    }


    private void sendNotification(PendingIntent pendingIntent,String title,String messageBody) {
        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
         getNotification(messageBody, pendingIntent, channelId, defaultSoundUri,title);
    }

    private void getNotification(String messageBody, PendingIntent pendingIntent, String channelId, Uri defaultSoundUri,String title) {
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                                R.drawable.newlogo))
                        .setContentTitle(title)
                        .setPriority(Notification.PRIORITY_MAX)
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.logo1);
            notificationBuilder.setColor(getResources().getColor(R.color.black));
        } else {
            notificationBuilder.setSmallIcon(R.drawable.logo);
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private PendingIntent getHomePendingIntent() {
        Intent groupIntent = new Intent(this, CardViewActivity.class);
        groupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, groupIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }
    private PendingIntent getPendingIntent(String groupId) {
        Intent groupIntent = new Intent(this, GroupView.class);
        groupIntent.putExtra("groupId", groupId);
        groupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, groupIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }
    private PendingIntent getGroupIntent(String groupId) {
        Intent groupIntent = new Intent(this, GroupView.class);
        groupIntent.putExtra("groupId", groupId);
        groupIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, groupIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }
    private PendingIntent getUserIntent(String userId) {
        Intent userIntent = new Intent(this, UserView.class);
        userIntent.putExtra("swamiUserId", userId);
        userIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, userIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }
    private PendingIntent getPadiIntent(String padiId) {
        Intent padiIntent = new Intent(this, PadiPoojaView.class);
        padiIntent.putExtra("padiPoojaId", padiId);
        padiIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, padiIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }
    private PendingIntent getTopicIntent(String topicId) {
        Intent topicIntent = new Intent(this, TopicView.class);
        topicIntent.putExtra("topicId", topicId);
        topicIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return PendingIntent.getActivity(this, 0 /* Request code */, topicIntent,
                PendingIntent.FLAG_ONE_SHOT);
    }
}