package com.zyot.fung.shyn;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import static com.zyot.fung.shyn.common.Constants.HOST_PORT;

public class TestGame {
    public static void main(String[] args) throws IOException {
//        GameSetup game = new GameSetup("FuDuSkyWar", 500, 600);
//        game.start();
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress("192.168.0.111", HOST_PORT), 10000);
    }
}
