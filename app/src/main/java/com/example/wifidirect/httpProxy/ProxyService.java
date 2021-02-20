package com.example.wifidirect.httpProxy;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.example.wifidirect.model.ConnectedDeviceList;
import com.example.wifidirect.GlobalNotificationsBuilder;
import com.example.wifidirect.net.IpNeighbour;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import androidx.annotation.Nullable;

import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;

public class ProxyService extends Service {
    private static final String TAG = "ProxyService";
    private ServerSocket socket;
    public static Boolean isTrue = true;
    public Notification notification;
    public ProxyService() throws IOException {
        socket = new ServerSocket(1080);
    }
    private ArrayList<ConnectedDeviceList> peerList;
    private int notification_id;




        @Override
    public void onCreate() {
        super.onCreate();

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        notification_id=69;
        GlobalNotificationsBuilder notificationsBuilder = new GlobalNotificationsBuilder(this,notification_id);
        this.notification = notificationsBuilder.createNotification();
        startForeground(notification_id,notification);

      /*  final ConnectivityManager connectivityManager = (ConnectivityManager) this.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder request = new NetworkRequest.Builder();
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
        connectivityManager.registerNetworkCallback(request.build(),new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(Network network) {
            /*    URL url = null;
                try {
                    url = new URL("https://duckduckgo.com");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    network.openConnection(url);
                } catch (IOException e) {
                    e.printStackTrace();
                }


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    connectivityManager.bindProcessToNetwork(network);
                }
                else {
                    connectivityManager.setProcessDefaultNetwork(network);
                }
            }
        });

*/


        new Thread() {
            public void run() {
                while (isTrue) {
                    Socket fd = null;
                    try {
                        fd = socket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ProxyConnection thread = new ProxyConnection(fd);
                }
            }
        }.start();

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        isTrue = false;
        try {
            socket.close();
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
