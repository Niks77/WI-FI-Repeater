package com.example.wifidirect.net;

import android.util.Log;

import com.example.wifidirect.GlobalPeerList;
import com.example.wifidirect.model.ConnectedDeviceList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class IpNeighbour extends  Thread{
    private static final String TAG = "IpNeighbour";

    public ArrayList<ConnectedDeviceList> getResult() {
        return result;
    }

    private ArrayList<ConnectedDeviceList> result;
    public IpNeighbour() {
            start();

    }


    @Override
    public void run() {


        Process process = null;
        try {
            process = Runtime.getRuntime().exec("ip neigh");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert process != null;
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        result = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                Log.d(TAG, "starts: " + line);
                String[] chunk = line.split(" ");
                if (chunk.length > 5) {
                    ConnectedDeviceList list = new ConnectedDeviceList();
                    list.setAddress(chunk[0]);
                    list.setWifiCard(chunk[2]);
                    list.setStatus(chunk[5]);
                    if (list.getAddress().startsWith("192.168.49") && list.getStatus().equals("REACHABLE")) {
                        result.add(list);
                        GlobalPeerList.setPeerAddress(chunk[0]);
                    }
                }
             }
        } catch (IOException e) {
            e.printStackTrace();
            }
        GlobalPeerList.setPeerList(result);
    }
}
