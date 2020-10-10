package com.example.wifidirect.message;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;

import com.example.wifidirect.util.Socks5Util;

public class Socks5CommandResponse {
    private static final String TAG = "Socks5CommandResponse";
    private InetAddress inetAddress;

    private int addressType;

    private int port;

    private int version;

    private int reply;

    public Socks5CommandResponse(int version, InetAddress inetAddress, int addressType, int port, int reply) {
        this.inetAddress = inetAddress;
        this.addressType = addressType;
        this.port = port;
        this.reply = reply;
        this.version = version;
    }

    public byte[] getBytes(){
        byte bytes[]= null;
        switch (addressType){
            case Socks5Reply.ADTY_IPV4:
                bytes = new byte[10];
                byte[] ipv4Bytes = inetAddress.getAddress();
                System.arraycopy(ipv4Bytes,0,bytes,4,ipv4Bytes.length);
                bytes[8] = Socks5Util.getFirstByteFromInt(port);
                bytes[9] = Socks5Util.getSecondByteFromInt(port);
                break;
            case Socks5Reply.ADTY_IPV6:
                bytes = new byte[22];
                byte[] ipv6Bytes = inetAddress.getAddress();
                System.arraycopy(ipv6Bytes,0,bytes,4,ipv6Bytes.length);
                bytes[20] = Socks5Util.getFirstByteFromInt(port);
                bytes[21] = Socks5Util.getSecondByteFromInt(port);
                break;
            case Socks5Reply.ADTY_DOMAIN_NAME:
                bytes = new byte[10];
                addressType = Socks5Reply.ADTY_IPV4;
                for(int i=4;i<10;i++){
                    bytes[i]=0;
                }

                break;
            default:
                break;
        }
        if(bytes != null){
            bytes[0] = (byte) version;
            bytes[1] = (byte) reply;
            bytes[2] = (byte) 0x00;
            bytes[3] = (byte) addressType;
        }
        return bytes;
    }
    public void send(OutputStream outputStream){
        try {
            outputStream.write(getBytes());
            outputStream.flush();
            Log.d(TAG, "send: response send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
