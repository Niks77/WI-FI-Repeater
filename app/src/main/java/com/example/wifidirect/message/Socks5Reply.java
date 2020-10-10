package com.example.wifidirect.message;

public class Socks5Reply {
    public static final int REQUEST_GRANTED = 0x00;
    public static final int GENERAL_SOCKS_FAILURE = 0x01;
    public static final int NETWORK_UNREACHABLE = 0x03;
    public static final int HOST_UNREACHABLE = 0x04;
    public static final int CONNECTION_REFUSED = 0x05;
    public static final int TTL_EXPIRED = 0x06;
    public static final int COMMAND_NOT_SUPPORTED = 0x07;
    public static final int ADDRESS_TYPE_NOT_SUPPORTED = 0x08;

    public static final int RESERVED = 0x00;
    public static final int ADTY_IPV4 = 0x01;
    public static final int ADTY_DOMAIN_NAME = 0x03;
    public static final int ADTY_IPV6 = 0x04;



}
