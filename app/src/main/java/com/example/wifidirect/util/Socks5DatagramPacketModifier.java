package com.example.wifidirect.util;

import com.example.wifidirect.message.Socks5Command;
import com.example.wifidirect.message.Socks5Reply;
import com.example.wifidirect.util.Socks5Util;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;

public class Socks5DatagramPacketModifier {
    public DatagramPacket encapsulate(DatagramPacket packet, InetAddress address ,int port){
        byte[] data = packet.getData();
        InetAddress remoteAddress = packet.getAddress();
        byte[] addressByte = remoteAddress.getAddress();
        int addressLength= addressByte.length;
        int remotePort = packet.getPort();
        byte[] buffer = new byte[6 + addressLength + packet.getLength()];
        buffer[0]=0;
        buffer[1]=0;
        buffer[2]=0;
        buffer[3]= (byte) (addressLength == 4 ? Socks5Reply.ADTY_IPV4: Socks5Reply.ADTY_IPV6);
        System.arraycopy(addressByte,0,buffer,4,addressLength);
        buffer[4+addressLength]= Socks5Util.getFirstByteFromInt(remotePort);
        buffer[5+addressLength]=Socks5Util.getSecondByteFromInt(remotePort);
        System.arraycopy(data,0,buffer,6+addressLength,packet.getLength());
        return new DatagramPacket(buffer,buffer.length,address,port);
    }
    public void decapsulate(DatagramPacket packet) throws Exception {
        byte[] data = packet.getData();
        if(!(data[0] == 0 && data[1]==0)){
            throw  new Exception("Packet is corrupted");
        }
        if(data[2]!=0){
            throw new Exception("Fragment Error");
        }
        InetAddress address = null;
        int port;
        byte[] orginalData = null;
        int length = 0;
        if(data[3] == Socks5Command.ADDRESS_IPV4){
            length = 4;
        }
        else {
            length = 6;
        }
        try {
            address = InetAddress.getByAddress(Arrays.copyOfRange(data, 4, 4+length));
        }
        catch (UnknownHostException e){
            e.printStackTrace();
        }
        port = Socks5Util.bytesToInt(data[4+length],data[5+length]);
        orginalData = Arrays.copyOfRange(data,6+length,packet.getLength());

        packet.setAddress(address);
        packet.setData(orginalData);
        packet.setPort(port);
    }
}
