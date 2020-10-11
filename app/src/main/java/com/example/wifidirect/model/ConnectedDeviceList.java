package com.example.wifidirect.model;

public class ConnectedDeviceList {
    private String address;
    private String wifiCard;
    private String status;
    private long prevBytes=0;
    private long nextBytes=0;
    private long totalBytes=0;
    private String speed;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWifiCard() {
        return wifiCard;
    }

    public void setWifiCard(String wifiCard) {
        this.wifiCard = wifiCard;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public long getPrevBytes() {
        return prevBytes;
    }

    public void setPrevBytes(long prevBytes) {
        this.prevBytes = prevBytes;
    }

    public long getNextBytes() {
        return nextBytes;
    }

    public void setNextBytes(long nextBytes) {
        this.nextBytes = nextBytes;
    }

    public long getTotalBytes() {
        return totalBytes;
    }

    public void setTotalBytes(long totalBytes) {
        this.totalBytes += totalBytes;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

}
