package com.example.wifidirect.fragment;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.wifidirect.IpNeighbourMonitor;
import com.example.wifidirect.R;
import com.example.wifidirect.autoConnect.AutoConnect;
import com.example.wifidirect.direct.DirectNetShare;
import com.example.wifidirect.httpProxy.ProxyService;
import com.example.wifidirect.sock5.Socks5ProxyService;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private CompoundButton wifi;
    private CompoundButton connection;
    private CompoundButton proxy;
    private CompoundButton socks5proxy;
    private DirectNetShare wifiDirect;
    private TextView textView;
    private boolean boolWifi = false;
    private boolean boolAuto = false;
    private boolean boolHttp = false;
    private boolean boolSock = false;
    private Context context;
    private DirectNetShare.GroupCreatedListener groupCreatedListener;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        assert container != null;
        init(view);
        EnableSwitch();
        wifi.setChecked(boolWifi);
        proxy.setChecked(boolHttp);
        connection.setChecked(boolAuto);
        socks5proxy.setChecked(boolSock);
        return view;
    }
    private void init(View container){
        this.wifi = container.findViewById(R.id.wifiSwitch);
        this.proxy = container.findViewById(R.id.proxyButton);
        this.connection = container.findViewById(R.id.autoConButton);
        this.textView = container.findViewById(R.id.textView);
        this.socks5proxy = container.findViewById(R.id.socks5Switch);
        context = container.getContext();
        groupCreatedListener = new DirectNetShare.GroupCreatedListener() {
            private String string;
            @Override
            public void onGroupCreated(String ssid, String password) {
                string = ssid + "\n" + password;
                textView.setText(string);
                Log.d(TAG, "onGroupCreated: " + string);
            }
        };
        wifiDirect = new DirectNetShare(context,groupCreatedListener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null) {
            boolWifi = savedInstanceState.getBoolean("boolWifi");
            boolAuto = savedInstanceState.getBoolean("boolAuto");
            boolHttp = savedInstanceState.getBoolean("boolHttp");
            boolSock = savedInstanceState.getBoolean("boolSock");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("boolWifi",boolWifi );
        outState.putBoolean("boolAuto",boolAuto );
        outState.putBoolean("boolHttp",boolHttp );
        outState.putBoolean("boolSock",boolSock );

    }

    private void EnableSwitch(){
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolWifi = isChecked;
                if(isChecked) {
                    startMonitorService();
                    wifiDirect.start();
                }
                else {
                    stopMonitorService();
                    wifiDirect.stop();
                    textView.setText("");
                }
            }

        });
        this.proxy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolHttp = isChecked;
                if(isChecked){
                    ProxyService.isTrue=true;
                    startHttpProxy();
                }
                else{
                    ProxyService.isTrue = false;
                    stopHttpProxy();
                }
            }
        });
        this.connection.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolAuto = isChecked;
                if(isChecked){
                    Log.d(TAG, "onCheckedChanged: true");
                    AutoConnect.isTrue = true;
                    startService();
                }
                else{
                    Log.d(TAG, "onCheckedChanged: false ");
                    AutoConnect.isTrue = false;
                    stopService();
                }
            }
        });
        this.socks5proxy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                boolSock = isChecked;
                if(isChecked){
                    Log.d(TAG, "onCheckedChanged: true" );
                    Socks5ProxyService.isTrue= true;
                    startSocks5Proxy();

                }
                else {
                    Log.d(TAG, "onCheckedChanged: false");
                    Socks5ProxyService.isTrue = false;
                    stopSocks5Proxy();
                }
            }
        });
    }
    private void startMonitorService(){
        Intent serviceIntent = new Intent(context, IpNeighbourMonitor.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
    private void startHttpProxy() {
        Intent serviceIntent = new Intent(context, ProxyService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
    private void startService(){
        Intent serviceIntent = new Intent(context, AutoConnect.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
    private void stopMonitorService(){
        Intent serviceIntent = new Intent(context, ProxyService.class);
        context.stopService(serviceIntent);
    }
    private void stopHttpProxy() {

        Intent serviceIntent = new Intent(context, IpNeighbourMonitor.class);
        context.stopService(serviceIntent);
    }
    private void stopService(){
        Intent serviceIntent = new Intent(context, AutoConnect.class);
        context.stopService(serviceIntent);

    }
    private void startSocks5Proxy(){
        Intent serviceIntent = new Intent(context, Socks5ProxyService.class);
        ContextCompat.startForegroundService(context, serviceIntent);
    }
    private void stopSocks5Proxy(){
        Intent serviceIntent = new Intent(context, Socks5ProxyService.class);
        context.stopService(serviceIntent);
    }
}
