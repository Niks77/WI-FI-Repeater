package com.example.wifidirect.io;

import android.util.Log;

import com.example.wifidirect.GlobalPeerList;
import com.example.wifidirect.util.Socks5DatagramPacketModifier;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

public class UDPRelay extends Thread {
    private static final String TAG = "UDPRelay";
    InetAddress inetAddress;
    int port;
    int port1;
    private DatagramSocket socket;
    boolean running;
    int bufferSize = 1024*64;
    boolean i = true;
    public UDPRelay(InetAddress  inetAddress, int port) {
        Log.d(TAG, "UDPRelay: ");
        this.inetAddress = inetAddress;
       // this.port = port;
    }

    public InetAddress getInetAddress() {
        return inetAddress;
    }

    public void setInetAddress(InetAddress inetAddress) {
        this.inetAddress = inetAddress;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public void setSocket(DatagramSocket socket) {
        this.socket = socket;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public SocketAddress startServer() throws SocketException {
        Log.d(TAG, "startServer: ");
        running = true;
        socket = new DatagramSocket();
        SocketAddress address =  socket.getLocalSocketAddress();
        socket.setSoTimeout(1000*30);
        start();
        return address;
    }

    @Override
    public void run() {
        Log.d(TAG, "run: ");
        try{
            byte[] buffer = new byte[bufferSize];
            while (running){
                DatagramPacket packet = new DatagramPacket(buffer,buffer.length);
                Log.d(TAG, "run: before packet");
                socket.receive(packet);
                Log.d(TAG, "run: after packet");
                Log.d(TAG, "run: client ip :" + packet.getAddress() +" client port :" + packet.getPort());

                Socks5DatagramPacketModifier datagramPacketModifier = new Socks5DatagramPacketModifier();
                if(isFromClient(packet)){
                    port1 = packet.getPort();
                    datagramPacketModifier.decapsulate(packet);
                    socket.send(packet);
                    Log.d(TAG, "run: send to server");
                }
                else {
                    packet = datagramPacketModifier.encapsulate(packet,this.inetAddress,this.port1);
                    socket.send(packet);
                    Log.d(TAG, "run:  send to client");
                }
            }

        }catch (SocketException e){
            running = false;
            socket.close();
        }
        catch (IOException e) {
            running = false;
            socket.close();
            e.printStackTrace();
        }  catch (Exception e) {
            running = false;
            socket.close();
            e.printStackTrace();
        }
    }




    private boolean isFromClient(DatagramPacket packet){
        Log.d(TAG, "isFromClient: "+ packet.getAddress().getHostAddress() + "  " + this.inetAddress.getHostAddress());
        if(packet.getAddress().getHostAddress().equals(this.inetAddress.getHostAddress())){

            return true;
        }
     return false;

   }
}
