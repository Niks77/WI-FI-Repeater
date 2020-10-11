package com.example.wifidirect.sock5;

import com.example.wifidirect.io.TCPStream;
import com.example.wifidirect.message.Socks5Command;
import com.example.wifidirect.message.Socks5CommandResponse;
import com.example.wifidirect.message.Socks5Reply;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class TcpConnect {
    private static final String TAG = "TcpConnect";
    public static final int UPLOAD_SPEED = 0;
    public static final int DOWNLOAD_SPEED = 1;
    private Socks5Command socks5Command;
    private Socket clientProxy;
    long upLinkBytes = 0;
    long downLinkBytes = 0;
    public TcpConnect(Socks5Command socks5Command,Socket clientProxy) {
        this.socks5Command  = socks5Command;
        this.clientProxy = clientProxy;
    }
    public void doConnect() throws InterruptedException, IOException {
        InetAddress address = socks5Command.getInetAddress();
        int port = socks5Command.getPort();
        int version = socks5Command.getVersion();
        int addressType = socks5Command.getAddressType();
        int reply= Socks5Reply.REQUEST_GRANTED;
        Socket proxyToServer = null;
        OutputStream outputStream = clientProxy.getOutputStream();
        try {
            proxyToServer= new Socket(address,port);
            // proxyToServer.setSoTimeout(10000);
        } catch (IOException e) {
            if (e.getMessage().equals("Connection refused")) {
                reply = Socks5Reply.CONNECTION_REFUSED;
            } else if (e.getMessage().equals("Operation timed out")) {
                reply = Socks5Reply.TTL_EXPIRED;
            } else if (e.getMessage().equals("Network is unreachable")) {
                reply = Socks5Reply.NETWORK_UNREACHABLE;
            } else if (e.getMessage().equals("Connection timed out")) {
                reply = Socks5Reply.TTL_EXPIRED;
            } else {
                reply = Socks5Reply.GENERAL_SOCKS_FAILURE;
            }
        }
        Socks5CommandResponse response = new Socks5CommandResponse(version,address,addressType,port,reply);

        response.send(outputStream);
        if(reply!=Socks5Reply.REQUEST_GRANTED){
            return;
        }
        TCPStream clientToServer = new TCPStream(clientProxy,proxyToServer, UPLOAD_SPEED);
        TCPStream serverToClient = new TCPStream(proxyToServer,clientProxy,DOWNLOAD_SPEED);


        clientToServer.start();
        serverToClient.start();

        serverToClient.join();
        clientToServer.join();

        //close the streams and sockets
        clientToServer.close();
        serverToClient.close();
    }



}
