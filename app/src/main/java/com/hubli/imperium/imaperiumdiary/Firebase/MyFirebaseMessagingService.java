package com.hubli.imperium.imaperiumdiary.Firebase;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.hubli.imperium.imaperiumdiary.Data.SPData;
import com.hubli.imperium.imaperiumdiary.Main.MainActivity;
import com.hubli.imperium.imaperiumdiary.Main.MainTabAdaptor;
import com.hubli.imperium.imaperiumdiary.Messaging.ClassMessaging;
import com.hubli.imperium.imaperiumdiary.Messaging.MessagingDB;
import com.hubli.imperium.imaperiumdiary.R;
import com.hubli.imperium.imaperiumdiary.Utility.MyApplication;

import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * Created by Faheem on 29-05-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        Map<String, String> data = remoteMessage.getData();Log.e("got",remoteMessage.getData().toString());
        if(data.get("notification_type").contentEquals("chat")){
            processChat(data);
        }else{
             new SPData().updateNotificationCount();
             Intent intent = new Intent(getApplicationContext(), MainActivity.class);
             sendNotification(data.get("notification_type"),data.get("notification_text"),intent,2);
            getApplicationContext().sendBroadcast(new Intent(MainTabAdaptor.NOTIFICATION_BC_FILTER));
        }
    }

    private void processChat(Map<String, String> data) {
        if(!data.get(SPData.USER_NUMBER).contentEquals(new SPData().getUserData(SPData.USER_NUMBER))) {
            Intent intent = new Intent(getApplicationContext(), ClassMessaging.class);
            sendNotification("Class Message", data.get(SPData.FIRST_NAME) + ": " + data.get("message"), intent, 1);
            MessagingDB db = new MessagingDB(getApplicationContext());
            db.insertNewMessage(data);
            sendBroadcast(new Intent("NEW_MESSAGE"));
        }
    }

    private void sendNotification(String title, String msg, Intent intent, int notification_id) {

        NotificationManager mNotificationManager;
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.defaultpropic)
                        .setContentTitle(title)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(notification_id, mBuilder.build());
    }
}
