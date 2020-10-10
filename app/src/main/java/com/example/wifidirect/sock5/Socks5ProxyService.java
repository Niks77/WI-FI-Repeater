package com.example.wifidirect.sock5;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.example.wifidirect.GlobalNotificationsBuilder;

import java.io.IOException;
import java.net.ServerSocket;

import androidx.annotation.Nullable;

public class Socks5ProxyService extends Service {
    private static final String TAG = "Socks5ProxyService";
    private ServerSocket serverSocket;
    private Notification notification;
    public Socks5ProxyService() {
        Log.d(TAG, "Socks5ProxyService: ");

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public static boolean isTrue;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        int notification_id=69;
        GlobalNotificationsBuilder notificationsBuilder = new GlobalNotificationsBuilder(this,notification_id);
        this.notification = notificationsBuilder.createNotification();
        startForeground(notification_id,notification);
        int numProc = Runtime.getRuntime().availableProcessors();
        try {
            Socks5Executor executor = new Socks5Executor(numProc);
            Thread thread = new Thread(executor);
            thread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return START_STICKY;
//    }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isTrue = false;
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
    }

}
