package com.example.wifidirect;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class GlobalNotificationsBuilder {
    private Context context;
    private Notification notification;
    private int Notification_id;
    public GlobalNotificationsBuilder(Context context,int Notification_id) {
        this.context = context;
        this.Notification_id = Notification_id;
    }
    public static int count=0;
    private String NOTIFICATION_CHANNEL_ID = "com.example.simpleapp";

    public Notification createNotification(){
        String channelName = "My Background Service";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(android.app.Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Wifi Repeater Service is Running")
                .setContentText(count + " Device connected ")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notification = builder.build();
        return notification;

    }
    public void updateNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID)
                .setContentTitle("Wifi Repeater Service is Running")
                .setContentText(count + " Device connected ")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_launcher_background);
        builder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notification = builder.build();
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(Notification_id,notification);

    }
}
