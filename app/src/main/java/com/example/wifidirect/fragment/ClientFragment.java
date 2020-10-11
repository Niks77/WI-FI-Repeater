package com.example.wifidirect.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wifidirect.GlobalPeerList;
import com.example.wifidirect.R;
import com.example.wifidirect.model.ConnectedDeviceList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ClientFragment extends Fragment {
    private static final String TAG = "ClientFragment";
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_device,container,false);
        updateSpeed();
        return view;
    }
    public void updateSpeed(){
        new Thread(){
            @Override
            public void run(){
                while (true){
                    for(ConnectedDeviceList deviceList : GlobalPeerList.getPeerList()){
                        deviceList.setNextBytes(0);
                        deviceList.setPrevBytes(0);
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for(ConnectedDeviceList deviceList : GlobalPeerList.getPeerList()){
                        deviceList.setSpeed(calculateSpeed(deviceList.getPrevBytes(),deviceList.getNextBytes()));
                        deviceList.setNextBytes(0);
                        deviceList.setPrevBytes(0);
                    }
                }

            }

            private String calculateSpeed(long prevBytes, long nextBytes) {
                long speed = (nextBytes - prevBytes)/3000 ;
                Log.d(TAG, "calculateSpeed: " + speed);
                return String.valueOf(speed) + "Kbps";
            }


        }.start();
    }
}
