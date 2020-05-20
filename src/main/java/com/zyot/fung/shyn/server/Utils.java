package com.zyot.fung.shyn.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Random;

public class Utils {
    public static int randomGeneratePort(int min, int max) {
        Random rand = new Random();
        return rand.nextInt((max - min) + 1) + min;
    }

    public static String getLocalAddress() {
        try {
            InetAddress inetAddress = InetAddress.getLocalHost();
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static boolean availablePort(int port) {
        boolean result = true;

        try {
            (new Socket(getLocalAddress(), port)).close();

            // Successful connection means the port is taken.
            result = false;
        }
        catch(SocketException e) {
            // Could not connect.
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void main(String[] args) {
        System.out.println(randomGeneratePort(40000, 49998));
    }
}
