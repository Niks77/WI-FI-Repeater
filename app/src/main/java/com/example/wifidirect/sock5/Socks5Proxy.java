package com.example.wifidirect.sock5;

import android.util.Log;

import com.example.wifidirect.message.ReadMessage;
import com.example.wifidirect.message.SendMessage;
import com.example.wifidirect.message.Socks5Command;
import com.example.wifidirect.message.Socks5CommandResponse;
import com.example.wifidirect.message.Socks5Reply;
import com.example.wifidirect.io.TCPStream;
import com.example.wifidirect.io.UDPRelay;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.CountDownLatch;

public class Socks5Proxy implements Runnable{
    private static final String TAG = "Socks5Proxy";
    private Socket clientProxy;
    private int cmd;
    private InputStream inputStream;
    private OutputStream outputStream;
    Socks5Proxy(Socket socket){
        this.clientProxy=socket;
//        try {
//            clientProxy.setSoTimeout(1000 * 60 * 5);
//        } catch (SocketException e) {
//            e.printStackTrace();
//        }
      //   start();
    }

    @Override
    public void run() {
        try {
            Log.d(TAG, "run: ");
            InputStream inputStream = this.clientProxy.getInputStream();
            OutputStream outputStream =this.clientProxy.getOutputStream();
            ReadMessage readMessage = new ReadMessage();
            readMessage.readHandshake(inputStream);
            SendMessage sendMessage = new SendMessage(readMessage.getHandVersion(),readMessage.getSocks5Auth());
            sendMessage.send(outputStream);
            Socks5Command socks5Command = new Socks5Command();
            socks5Command.read(inputStream);
            cmd=socks5Command.getCommand();
            Log.d(TAG, "run: cmd " + cmd);
            switch (cmd){
                case Socks5Command.TCP_CONNECTION:
                    tcpConnect(socks5Command);
                    break;
                case Socks5Command.TCP_PORT_BINDING:
                    break;
                case Socks5Command.UDP_CONNECTION:
                    udpConnect(socks5Command);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tcpConnect(Socks5Command socks5Command) throws InterruptedException, IOException {
        Log.d(TAG, "tcpConnect: ");
        InetAddress address = socks5Command.getInetAddress();
        int port = socks5Command.getPort();
        int version = socks5Command.getVersion();
        int addressType = socks5Command.getAddressType();
//        InetAddress temp = InetAddress.getByName("193.161.193.99");
//        if(address.getHostAddress().equals(temp.getHostAddress())) {
//            port= 38617;
//        }
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
        TCPStream clientToServer= new TCPStream(clientProxy,proxyToServer);//upload speed
        TCPStream serverToClient = new TCPStream(proxyToServer,clientProxy);//download speed
        clientToServer.start();
        serverToClient.start();

        serverToClient.join();
        clientToServer.join();

        //close the streams and sockets
        clientToServer.close();
        serverToClient.close();
    }
    private void udpConnect(Socks5Command socks5Command) throws IOException, InterruptedException {
        Log.d(TAG, "udpConnect: ");
        //CountDownLatch latch = new CountDownLatch(2);
        UDPRelay udpRelay = new UDPRelay(((InetSocketAddress)clientProxy.getRemoteSocketAddress()).getAddress(),
                socks5Command.getPort());
        Log.d(TAG, ((InetSocketAddress) clientProxy.getRemoteSocketAddress()).getAddress().getHostAddress() + " "+ socks5Command.getPort());
        OutputStream outputStream = clientProxy.getOutputStream();
        InetSocketAddress address = (InetSocketAddress) udpRelay.startServer();
        InetAddress ip = socks5Command.getInetAddress();
        //InetAddress ip = InetAddress.getByName("192.168.49.1");
        Socks5CommandResponse response = new Socks5CommandResponse(socks5Command.getVersion(),
               ip , socks5Command.getAddressType(), address.getPort(), Socks5Reply.REQUEST_GRANTED);
        Log.d(TAG, "udpConnect: + send response");
        response.send(outputStream);
        //latch.await();
        udpRelay.join();
        clientProxy.close();

        //close sockets
//        while (udpRelay.isRunning()){
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                clientProxy.close();
//                udpRelay.stopServer();
//                e.printStackTrace();
//            }
//            if(!clientProxy.isClosed()){
//                clientProxy.close();
//                udpRelay.stopServer();
//            }
//
//        }


    }
}
