package com.example.wifidirect.sock5;

import android.util.Log;

import com.example.wifidirect.GlobalPeerList;
import com.example.wifidirect.message.ReadMessage;
import com.example.wifidirect.message.SendMessage;
import com.example.wifidirect.message.Socks5Command;
import com.example.wifidirect.message.Socks5CommandResponse;
import com.example.wifidirect.message.Socks5Reply;
import com.example.wifidirect.io.UDPRelay;
import com.example.wifidirect.util.AddressUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.CountDownLatch;

public class Socks5Proxy extends Thread{
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
         start();
    }

    @Override
    public void run() {
        try {
            Log.d(TAG, "run: ");
            InputStream inputStream = this.clientProxy.getInputStream();
            OutputStream outputStream =this.clientProxy.getOutputStream();

            userEntryInList(clientProxy.getRemoteSocketAddress());

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
                    TcpConnect tcpConnect = new TcpConnect(socks5Command,clientProxy);
                    tcpConnect.doConnect();
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

    private void userEntryInList(SocketAddress remoteSocketAddress) {
        if(!GlobalPeerList.checkPeers((InetSocketAddress) remoteSocketAddress)){
            GlobalPeerList.setPeerAddress(AddressUtils.InetSocketAddressToString(remoteSocketAddress));
        }
    }

    private void udpConnect(Socks5Command socks5Command) throws IOException, InterruptedException {
        Log.d(TAG, "udpConnect: ");
        CountDownLatch latch = new CountDownLatch(2);
        UDPRelay udpRelay = new UDPRelay(((InetSocketAddress)clientProxy.getRemoteSocketAddress()).getAddress(),
                socks5Command.getPort(),latch);

        OutputStream outputStream = clientProxy.getOutputStream();
        InetSocketAddress address = (InetSocketAddress) udpRelay.startServer();
        InetAddress ip = socks5Command.getInetAddress();
        //InetAddress ip = InetAddress.getByName("192.168.49.1");
        Socks5CommandResponse response = new Socks5CommandResponse(socks5Command.getVersion(),
               ip , socks5Command.getAddressType(), address.getPort(), Socks5Reply.REQUEST_GRANTED);
        Log.d(TAG, "udpConnect: + send response");
        response.send(outputStream);
        latch.await();
        udpRelay.stopServer();
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
