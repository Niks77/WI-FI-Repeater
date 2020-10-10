package com.example.wifidirect.test;

import android.content.Context;

import com.example.wifidirect.httpProxy.ProxyConnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ProxyWorker extends Worker {
    private ServerSocket socket;
    private Boolean isTrue = true;
    public ProxyWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) throws IOException {
        super(context, workerParams);
        socket = new ServerSocket(6969);
    }

    @Override
    public void onStopped() {
        super.onStopped();
        stopProxy();
    }

    @NonNull
    @Override
    public Result doWork() {
        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.success();
    }

    private void init() throws IOException {
    while(true){
        Socket fd = this.socket.accept();
        ProxyConnection thread = new ProxyConnection(fd);
    }
    }
    public void stopProxy() {
        isTrue = false;
        try {
            socket.close();
            socket = null;
        } catch (Exception e) {/*ignore*/}
    }
}
