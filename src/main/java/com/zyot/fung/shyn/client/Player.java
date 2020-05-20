package com.zyot.fung.shyn.client;

import com.zyot.fung.shyn.packet.ReadyRequestPacket;
import com.zyot.fung.shyn.packet.StartGameRequestPacket;

public class Player extends Client {
    public boolean isReady;

    public Player(String host, int port) {
        super(host, port);
        isReady = false;
    }

    public void notifyReadyState(boolean isReady) {
        this.isReady = isReady;
        System.out.println(isReady);
        ReadyRequestPacket readyRequestPacket = new ReadyRequestPacket(super.getId(), isReady);
        super.sendObject(readyRequestPacket);
    }

    public void sendStartGameRequest(int level) {
        StartGameRequestPacket startGameRequestPacket = new StartGameRequestPacket(level);
        sendObject(startGameRequestPacket);
    }
}
