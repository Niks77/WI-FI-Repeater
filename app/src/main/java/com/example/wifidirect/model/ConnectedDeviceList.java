package com.example.wifidirect.model;

public class ConnectedDeviceList {
    private String address;
    private String wifiCard;
    private String status;
    private long bytes;
    private String speed;
    private String totalSize;

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

    public long getBytes() {
        return bytes;
    }

    public void setBytes(long bytes) {
        this.bytes = bytes;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(String totalSize) {
        this.totalSize = totalSize;
    }
}
