package com.example.wifidirect.message;

import com.example.wifidirect.util.Socks5Util;

import java.io.IOException;
import java.io.InputStream;

public class ReadMessage {

    private int handVersion;
    private int socks5AuthNumber;
    private int socks5Auth;

    public void readHandshake(InputStream inputStream) throws IOException {
        this.handVersion = Socks5Util.readLine(inputStream.read());
        this.socks5AuthNumber = Socks5Util.readLine(inputStream.read());
        this.socks5Auth=Socks5Util.readLine(inputStream.read());

    }

    public int getHandVersion() {
        return handVersion;
    }

    public int getSocks5AuthNumber() {
        return socks5AuthNumber;
    }

    public int getSocks5Auth() {
        return socks5Auth;
    }
}
