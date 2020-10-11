package com.example.wifidirect.message;

import android.util.Log;

import com.example.wifidirect.util.Socks5Util;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.example.wifidirect.util.Socks5Util.readLine;

public class Socks5Command {
    private static final String TAG = "Socks5Command";

    public static final int VERSION_SOCKS5 = 0x05;

    public static final int VERSION_SOCKS4 = 0x04;

    public static final int TCP_CONNECTION = 0x01;

    public static final int TCP_PORT_BINDING = 0X02;

    public static final int UDP_CONNECTION = 0x03;

    public static final int RESERVE = 0x00;

    public static final int ADDRESS_IPV4 =0x01;

    public static final int ADDRESS_DOMAIN = 0X03;

    public static final int ADDRESS_IPV6 = 0X04;

    private int addressType;

    private int port;

    private InetAddress inetAddress;

    private String host;

    private int command;

    private int version;

    private int reserve;


    public byte[] getBytes(){
        byte bytes[]= null;
        switch (addressType){
            case ADDRESS_IPV4:
                bytes = new byte[10];
                byte[] ipv4Bytes = inetAddress.getAddress();
                System.arraycopy(ipv4Bytes,0,bytes,4,bytes.length);
                bytes[8] = Socks5Util.getFirstByteFromInt(port);
                bytes[9] = Socks5Util.getSecondByteFromInt(port);
                break;
            case ADDRESS_IPV6:
                bytes = new byte[22];
                byte[] ipv6Bytes = inetAddress.getAddress();
                System.arraycopy(ipv6Bytes,0,bytes,4,ipv6Bytes.length);
                bytes[20] = Socks5Util.getFirstByteFromInt(port);
                bytes[21] = Socks5Util.getSecondByteFromInt(port);
                break;
            case ADDRESS_DOMAIN:
                final int hostLength = host.getBytes().length;
                bytes = new byte[7+hostLength];
                bytes[4] = (byte) hostLength;
                for(int i=0;i<hostLength;i++){
                    bytes[5 + i] = host.getBytes()[i];
                }
                bytes[5 + hostLength] = Socks5Util.getFirstByteFromInt(port);
                bytes[6 + hostLength] = Socks5Util.getSecondByteFromInt(port);
                break;
            default:
                break;
        }
        if(bytes != null){
            bytes[0] = VERSION_SOCKS5;
            bytes[1] = (byte) command;
            bytes[2] = (byte) RESERVE;
            bytes[3] = (byte) addressType;
        }
        return bytes;
    }

    public void read(InputStream inputStream) throws IOException {

        version = readLine(inputStream.read());
        int cmd = readLine(inputStream.read());
        Log.d(TAG, "read: cmd " + version);

        switch (cmd){
            case TCP_CONNECTION:
                command = TCP_CONNECTION;
                break;
            case TCP_PORT_BINDING:
                command = TCP_PORT_BINDING;
                break;
            case UDP_CONNECTION:
                command = UDP_CONNECTION;
                break;
            default:
                //TODO send reply
        }
        Log.d(TAG, "read: command " + command);
        reserve = readLine(inputStream.read());
        addressType = readLine(inputStream.read());
        Log.d(TAG, "read: " + version + " " + cmd + " " + " " + reserve + " " + addressType);
        if(!isAddressSupported(addressType)){
            // TODO reply with exception
        }
        switch (addressType){
            case ADDRESS_IPV4:
                Log.d(TAG, "read: ipv4");
                byte[] addressIPV4= Socks5Util.read(inputStream,4);
                inetAddress = InetAddress.getByAddress(addressIPV4);
                break;

            case ADDRESS_IPV6:
                Log.d(TAG, "read: ipv6");
                byte[] addressIPV6 = Socks5Util.read(inputStream,16);
                inetAddress = InetAddress.getByAddress(addressIPV6);
                break;
            case ADDRESS_DOMAIN:
                Log.d(TAG, "read: domainName");
                int domainLength = Socks5Util.readLine(inputStream.read());
                Log.d(TAG, "read: length " + domainLength);
                byte[] addressDomain = Socks5Util.read(inputStream,domainLength);
                Log.e(TAG, "read: length" );
                host = new String(addressDomain, StandardCharsets.UTF_8);
                try {
                    inetAddress = InetAddress.getByName(host);
                }catch (UnknownHostException e){
                    e.printStackTrace();
                }
                // TODO reply with error message
                break;
            default:
                break;
        }
        byte[] portBytes = Socks5Util.read(inputStream,2);
        Log.d(TAG, "read: portBytes " + portBytes.length);
        try{
            port = Socks5Util.bytesToInt(portBytes[0],portBytes[1]);
        }catch (Exception e){
           // Log.e(TAG, "read: port number packet is corrupted or illegal ",e );
            e.printStackTrace();
        }



    }

    private boolean isAddressSupported(int addressType){
        return addressType == ADDRESS_IPV4 || addressType ==ADDRESS_DOMAIN || addressType == ADDRESS_IPV6;
    }

    public int getAddressType() {
        return addressType;
    }

    public void setAddressType(int addressType) {
        this.addressType = addressType;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }
}
