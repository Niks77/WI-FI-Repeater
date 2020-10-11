package com.example.wifidirect.util;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

public class AddressUtils {
    public static String InetSocketAddressToString(InetSocketAddress address){
        return address.getAddress().getHostAddress().substring(1);
    }
    public static String InetSocketAddressToString(SocketAddress address){
        InetSocketAddress inetSocketAddress = (InetSocketAddress) address;
        return inetSocketAddress.getAddress().getHostAddress().substring(1);
    }
    public static String InetAddressToString(InetAddress inetAddress){
        return inetAddress.getHostAddress().substring(1);
    }

}
