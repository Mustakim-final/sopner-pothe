package com.example.ourproject.NotificationCreate;

import android.app.Notification;
import android.app.NotificationManager;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.ourproject.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessaging extends FirebaseMessagingService {
    public static final String TAG="my token";
    public static final String FCM_CHANNEL_ID="FCM_CHANNEL_ID";
    public static final String CHANNEL_NAME="CHAT_APP";
    private NotificationManager notificationManager;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG,"Message Call");
        Log.d(TAG,"Message Received form: "+remoteMessage.getFrom());
        //FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();

        if (remoteMessage.getNotification()!=null){
            String title=remoteMessage.getNotification().getTitle();
            String body=remoteMessage.getNotification().getBody();

            Log.d(TAG,"das;hfas: "+title);
            Log.d(TAG,"das;hfas: "+body);

            Notification notification=new NotificationCompat.Builder(this,FCM_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setContentTitle(title)
                    .setContentText(body)
                    .setColor(Color.BLUE)
                    .build();

            NotificationManager manager= (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            manager.notify(1002,notification);
        }


        if (remoteMessage.getData().size()>0){
            Log.d(TAG,"Message Received Data: "+remoteMessage.getData().size());

            for (String key:remoteMessage.getData().keySet()){
                Log.d(TAG,"Message Key: "+key+" Data: "+remoteMessage.getData().get(key));
            }

            Log.d(TAG,"Message Received Data: "+remoteMessage.getData().toString());
        }

    }


    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }
}
