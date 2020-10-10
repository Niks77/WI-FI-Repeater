package com.example.wifidirect;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.wifidirect.model.ConnectedDeviceList;
import com.example.wifidirect.net.IpNeighbour;

import java.util.ArrayList;

import androidx.annotation.Nullable;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;

public class IpNeighbourMonitor extends Service {
    private static final String TAG = "IpNeighbourMonitor";
    private ArrayList<ConnectedDeviceList> peerList;
    private int notification_id;

    private  final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "onReceive: ");
            updatePeerList(action);

        }

    };
    public void updatePeerList(final String action){
        new Thread() {
            @Override
            public void run() {
                assert action != null;
                if (action.equals(WIFI_P2P_CONNECTION_CHANGED_ACTION)) {
                    IpNeighbour ipNeighbour = new IpNeighbour();
                    try {
                        ipNeighbour.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    peerList = ipNeighbour.getResult();
                    updateNotification();
                }
            }
        }.start();
    }
    private void updateNotification() {
        GlobalNotificationsBuilder globalNotificationsBuilder = new GlobalNotificationsBuilder(this,
                notification_id);
        GlobalNotificationsBuilder.count = peerList.size();
        globalNotificationsBuilder.updateNotification();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
        filter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        registerReceiver(receiver,filter);
    }

    private Notification notification;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        notification_id=69;
        GlobalNotificationsBuilder notificationsBuilder = new GlobalNotificationsBuilder(this,notification_id);
        this.notification = notificationsBuilder.createNotification();
        startForeground(notification_id,notification);


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
