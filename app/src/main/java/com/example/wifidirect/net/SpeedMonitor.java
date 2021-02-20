package com.example.wifidirect.net;

public interface SpeedMonitor {
int bytes = 0;
int time = 0;
void calculateSpeed(int bytes);
}
