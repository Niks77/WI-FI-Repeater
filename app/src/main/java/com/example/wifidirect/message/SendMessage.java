package com.example.wifidirect.message;

import java.io.IOException;
import java.io.OutputStream;

public class SendMessage {
    private int version;
    private int authMethod;


    public SendMessage(int version, int authMethod) {
        this.version = version;
        this.authMethod = authMethod;
    }

    public byte[] getBytes(){
        byte[] bytes=new byte[2];
        bytes[0]= (byte) version;
        bytes[1]= (byte) authMethod;
        return bytes;
    }
    public void send(OutputStream outputStream) throws IOException {
        if(isValidVersion()) {
            outputStream.write(getBytes());
            outputStream.flush();
        }
        else{
            try {
                throw new Exception("Wrong SocksVersion");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public boolean isValidVersion(){
        return version==Socks5Request.VERSION_SOCKS5 || version == Socks5Request.VERSION_SOCKS4;
    }
}
