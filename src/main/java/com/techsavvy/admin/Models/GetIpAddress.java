package com.techsavvy.admin.Models;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetIpAddress {

    private final LocalStorage localStorage = new LocalStorage();

    public String getIpAddressServer() {
        String ipAddressServer = null;
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            ipAddressServer = inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            System.out.println("Could not find IP address: " + e.getMessage());
        }

        return ipAddressServer;
    }


}
