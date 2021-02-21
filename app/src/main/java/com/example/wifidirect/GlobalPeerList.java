package com.example.wifidirect;

import android.util.Log;

import com.example.wifidirect.model.ConnectedDeviceList;
import com.example.wifidirect.util.AddressUtils;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class GlobalPeerList {
    private static final String TAG = "GlobalPeerList";
    private static ArrayList<ConnectedDeviceList> peerList = new ArrayList<>();
    private static HashMap<String,String> peersAddress = new HashMap<>();
    public static boolean checkPeers(String address) {
        Log.d(TAG, "checkPeers: " + address);
        if(address.charAt(0) == '/') {
            address = address.substring(1);
        }
        if(peersAddress.containsKey(address)){
            return true;
        }
        return false;
    }
    public static boolean checkPeers(InetSocketAddress address){
        String strAddress = AddressUtils.InetSocketAddressToString(address);
        if(peersAddress.containsKey(strAddress)){
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
