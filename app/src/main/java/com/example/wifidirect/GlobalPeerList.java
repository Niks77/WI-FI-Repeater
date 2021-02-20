package com.example.wifidirect;

import android.util.Log;

import com.example.wifidirect.model.ConnectedDeviceList;

import java.util.ArrayList;
import java.util.HashMap;

public class GlobalPeerList {
    private static final String TAG = "GlobalPeerList";
    private static ArrayList<ConnectedDeviceList> peerList;
    private static HashMap<String,String> peersAddress = new HashMap<>();
    public static boolean checkPeers(String peerAddress) {
        Log.d(TAG, "checkPeers: " + peerAddress);
        String fix = peerAddress.substring(1);
        Log.d(TAG, "checkPeers: " + fix);
        if(peersAddress.containsKey(fix)){
            return true;
        }
        return false;
    }

    public static void setPeerAddress(String peerAddress) {
        peersAddress.put(peerAddress,"1");

    }


    public static ArrayList<ConnectedDeviceList> getPeerList() {
        return peerList;
    }

    public static void setPeerList(ArrayList<ConnectedDeviceList> peerLists) {
        peerList = peerLists;
    }
}
