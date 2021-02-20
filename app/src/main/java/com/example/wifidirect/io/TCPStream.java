package com.example.wifidirect.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPStream extends Thread {
    private Socket fromSocket;
    private Socket toSocket;
    private long size = 0;


    public TCPStream(Socket fromSocket, Socket toSocket) {
        this.fromSocket = fromSocket;
        this.toSocket = toSocket;
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
                this.size += read;
                toOutput.write(buffer, 0, read);
                if (fromInput.available() < 1) {
                    toOutput.flush();
                }
            }
        }
    }

    public long getSize() {
        return size;
    }

    public void close() throws IOException {
        fromSocket.close();
        toSocket.close();
    }
}
