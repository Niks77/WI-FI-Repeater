package com.example.wifidirect.io;

import com.example.wifidirect.GlobalPeerList;
import com.example.wifidirect.model.ConnectedDeviceList;
import com.example.wifidirect.sock5.TcpConnect;
import com.example.wifidirect.util.AddressUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPStream extends Thread {
    private Socket fromSocket;
    private Socket toSocket;
    private int linkType;


    public TCPStream(Socket fromSocket, Socket toSocket,int linkType) {
        this.fromSocket = fromSocket;
        this.toSocket = toSocket;
        this.linkType = linkType;
    }

    @Override
    public void run() {
        try{
            InputStream fromInput = this.fromSocket.getInputStream();
            OutputStream toOutput = this.toSocket.getOutputStream();
            readWrite(fromInput,toOutput);
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void readWrite(InputStream fromInput,OutputStream toOutput) throws IOException {
        byte[] buffer = new byte[4096];
        int read =0;
        while (read>=0) {
            read = fromInput.read(buffer);
            if (read>0) {
                toOutput.write(buffer, 0, read);
                if (fromInput.available() < 1) {
                    toOutput.flush();
                }
            }
            transferredSize(read);
        }
    }
    private void transferredSize(int size){
        ConnectedDeviceList deviceList = null;
        if(GlobalPeerList.getPeerList() != null){
            if(linkType == TcpConnect.DOWNLOAD_SPEED){

                for(ConnectedDeviceList deviceListIterator : GlobalPeerList.getPeerList()){
                    if(deviceListIterator.getAddress().equals(AddressUtils.InetSocketAddressToString(toSocket.getRemoteSocketAddress()))){
                        deviceList = deviceListIterator;
                        break;
                    }
                }

            }
            else if( linkType == TcpConnect.UPLOAD_SPEED){
                for(ConnectedDeviceList deviceListIterator : GlobalPeerList.getPeerList()){
                    if(deviceListIterator.getAddress().equals(AddressUtils.InetSocketAddressToString(fromSocket.getRemoteSocketAddress()))){
                        deviceList = deviceListIterator;
                        break;
                    }
                }
            }

        }
       if(deviceList!= null) {
           deviceList.setNextBytes(size);
           deviceList.setTotalBytes(size);
       }
    }

    public void close() throws IOException {
        fromSocket.close();
        toSocket.close();
    }


}
