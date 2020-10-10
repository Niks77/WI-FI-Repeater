package com.example.wifidirect.autoConnect;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class AutoConThread extends Thread {
    private static final String TAG = "AutoConThread";
    private Socket socket;
    private int command;
    private String wifiString="";
    private ConnectWifi connectWifi;
    private Context context;
    public void WifiLock(){
        connectWifi.setWifiLock();
    }
    public AutoConThread(Socket socket,Context context) {
        this.socket = socket;
        this.context = context;
        connectWifi = new ConnectWifi(context);
        Log.d(TAG, "AutoConThread: ");

    }
    public void Connect(){
        start();
    }
    public void CheckWifiConnection(){
        if(connectWifi.isWifiConnected()){
          wifiString = "Wifi is Connected";
        }
        else {
            wifiString = "Wifi is Not Connected";
        }
    }
    @Override
    public void run() {
        if(AutoConnect.isTrue) {
            try {
                byte[] buffer = new byte[4096];
                String string="";
                InputStream in = this.socket.getInputStream();
                do {
                    int read = in.read(buffer);
                    if(read == -1){
                        break;
                    }
                    string = string + new String(buffer,0,buffer.length);
                }
                while (in.available() > 0);
                //ParseRequest parseRequest = new ParseRequest(string);
                HeaderParsing parsing = new HeaderParsing(string);
                command = parsing.value;
               /* final ConnectivityManager connectivityManager = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkRequest.Builder request = new NetworkRequest.Builder();
                request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);
                connectivityManager.registerNetworkCallback(request.build(),new ConnectivityManager.NetworkCallback(){
                    @Override
                    public void onAvailable(Network network) {
                        URL url = null;
                        try {
                            url = new URL("https://duckduckgo.com");
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        try {
                            network.openConnection(url);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            connectivityManager.bindProcessToNetwork(network);
                        }
                        else {
                            connectivityManager.setProcessDefaultNetwork(network);
                        }
                    }
                });

                */
                Log.d(TAG, "run: " + string);
                OutputStream out = this.socket.getOutputStream();
                CheckWifiConnection();
                //connectWifi.start();
                String s = wifiString;
                out.write(("HTTP/1.1 200 OK\nContent-Type: text/plain\nContent-Length: " + s.length() + "\n\n" + s).getBytes());
                out.flush();
                out.close();
                if(command==0) {
                    Log.d(TAG, "run: command " + command);
                    new Thread() {
                        public void run() {
                            connectWifi.start();
                        }
                    }.start();
                }
                else {
                    Log.d(TAG, "run: command " + command);
                    new Thread() {
                        public void run() {
                            connectWifi.disConnect();
                        }
                    }.start();

                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
