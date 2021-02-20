package com.example.wifidirect.util;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

public class Socks5Util {
    private static final String TAG = "Socks5Util";
    public static byte  getFirstByteFromInt(int num){
        return (byte) ((num & 0xff00) >> 8);
    }
    public static byte getSecondByteFromInt(int num){
        return (byte) (num & 0x00ff);
    }
    public static int readLine(int value) throws IOException {
        if(value<0){

            throw new IOException("End of Stream");
        }
        else {
            Log.d(TAG, "readLine: " + value);
            return value;
        }
    }
    public static byte[] read(InputStream inputStream , int length) throws IOException {
        byte[] bytes = new byte[length];
        Log.d(TAG, "read: ");
        for(int i=0;i<length;i++){
            bytes[i] = (byte) readLine(inputStream.read());
            Log.d(TAG, "read: i " +i + " " +bytes[i]);
        }
        return bytes;
    }
    public static int bytesToInt(byte b1, byte b2){
        return (int) (b1 & 0xff) << 8 | (int) (b2 & 0xff);
    }

}
