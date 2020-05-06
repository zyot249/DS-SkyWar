package com.zyot.fung.shyn.client;

import com.zyot.fung.shyn.packet.ReadyRequestPacket;

public class Player extends Client {

    public Player(String host, int port) {
        super(host, port);
    }

    public void notifyReadyState(boolean isReady) {
        ReadyRequestPacket readyRequestPacket = new ReadyRequestPacket(super.getId(), isReady);
        super.sendObject(readyRequestPacket);
    }
}
