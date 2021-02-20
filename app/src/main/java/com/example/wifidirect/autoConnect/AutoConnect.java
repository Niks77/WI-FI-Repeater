package com.example.wifidirect.autoConnect;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.wifidirect.GlobalNotificationsBuilder;

import java.io.IOException;
import java.net.ServerSocket;

import androidx.annotation.Nullable;

public class AutoConnect extends Service {
    private static final String TAG = "AutoConnect";
    public static boolean isTrue = true;
    private ServerSocket socketCon;
    public Notification notification;
    public AutoConnect() throws IOException {
        socketCon = new ServerSocket(1081);
        Log.d(TAG, "AutoConnect: ");
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: auto");
        int notification_id=69;
        GlobalNotificationsBuilder notificationsBuilder = new GlobalNotificationsBuilder(this,notification_id);
        this.notification = notificationsBuilder.createNotification();
        startForeground(notification_id,notification);

            new Thread() {
            public void run() {
                while (isTrue) {
                    try {
                        AutoConThread conThread = new  AutoConThread(socketCon.accept(),getBaseContext());
                        conThread.Connect();
                        conThread.WifiLock();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //ProxyConnection thread = new ProxyConnection(fd);

                }
            }
        }.start();

        return START_STICKY;
//    }
//   public static void  onConnect(){
//        new Thread() {
//            public void run() {
//                connectWifi.start();
//            }
//        };
  }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isTrue = false;
        try {
            socketCon.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
