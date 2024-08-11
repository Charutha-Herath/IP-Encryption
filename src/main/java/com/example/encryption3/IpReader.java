package com.example.encryption3;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpReader {
    public static String getIPAddress() {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            return ip.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return null;
        }
    }
}
