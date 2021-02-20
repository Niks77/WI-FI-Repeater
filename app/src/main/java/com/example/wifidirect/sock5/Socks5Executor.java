package com.example.wifidirect.sock5;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Socks5Executor implements Runnable {
    private ServerSocket serverSocket;
    private ExecutorService pool;
    public Socks5Executor(int poolSize) throws IOException {
        this.serverSocket  = new ServerSocket(1083);
        pool = Executors.newCachedThreadPool();
    }

    @Override
    public void run() {
        while(true){
            try {
                pool.execute(new Socks5Proxy(serverSocket.accept()));
               // Socks5Proxy proxy = new Socks5Proxy(this.serverSocket.accept());
                //proxy.start();
            } catch (IOException e) {
                e.printStackTrace();
                pool.shutdown();
            }
        }
    }
}
