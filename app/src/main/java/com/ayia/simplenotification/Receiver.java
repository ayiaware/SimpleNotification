package com.ayia.simplenotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import static android.app.NotificationManager.IMPORTANCE_DEFAULT;

public class Receiver extends BroadcastReceiver{

    public static String NOTIFICATION_ID = "notification-id" ;

    public static final String NOTIFICATION_CHANNEL_ID = "notification-channel-id" ;

    public static final String NOTIFICATION_CONTENT = "content" ;

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent mainIntent = new Intent(context, MainActivity.class);

        String content = intent.getStringExtra(NOTIFICATION_CONTENT);

        int id = intent.getIntExtra( NOTIFICATION_ID , 0 ) ;

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(mainIntent);

        PendingIntent mainPendingIntent =
                PendingIntent.getActivity(
                        context,
                        id,
                        mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        Notification.Builder builder = new Notification.Builder(context);

        Notification notification = builder.setContentTitle("countdown")
                .setContentText(content)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.drawable.ic_twotone_timelapse_24)
                .setContentIntent(mainPendingIntent).build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(NOTIFICATION_CHANNEL_ID);
        }

        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "countdowns",
                IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(id, notification);
    }



}
