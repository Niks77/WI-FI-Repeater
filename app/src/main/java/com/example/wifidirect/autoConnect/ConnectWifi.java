package com.example.wifidirect.autoConnect;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class ConnectWifi {
    private static final String TAG = "ConnectWifi";
    private Context context;
    private WifiManager wifiManager;
    private WifiManager.WifiLock wifiLock;

    public ConnectWifi(Context context) {
        this.context = context;
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

    }

    public void setWifiLock() {
        wifiLock = ((WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "myLock");
        wifiLock.acquire();
    }

    public void enableWifi() {

        if (!isWifiEnable()) {
            wifiManager.setWifiEnabled(true);
        }
    }

    public boolean isWifiEnable() {
        return wifiManager.isWifiEnabled();
    }

    public boolean isWifiConnected() {
        if (isWifiEnable()) {
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getNetworkId() == -1)
                return false;
            return true;
        }
        return false;
    }

    public void stop() {
        wifiLock.release();
    }

    public void start() {
        enableWifi();
        connect();
        //turnOnHotspot();
        //turnOffHotspot();
    }

    public void connect() {
        WifiConfiguration wfc = new WifiConfiguration();
        Log.d(TAG, "connect: wifi");
        String key = "Express Wi-Fi by Facebook";
        wfc.SSID = String.format("\"%s\"", key);
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int netId = wifiManager.addNetwork(wfc);

        wifiManager.disconnect();
        wifiManager.enableNetwork(netId, true);
        wifiManager.reconnect();
    }
    public void disConnect(){
        WifiConfiguration wfc = new WifiConfiguration();
        Log.d(TAG, "connect: wifi");
        String key = "Express Wi-Fi by Facebook";
        wfc.SSID = String.format("\"%s\"", key);
        wfc.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        int netId = wifiManager.addNetwork(wfc);
        wifiManager.removeNetwork(netId);
    }
}
