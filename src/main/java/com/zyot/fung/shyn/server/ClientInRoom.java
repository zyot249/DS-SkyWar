package com.zyot.fung.shyn.server;

import java.io.Serializable;

public class ClientInRoom implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public String playerName;
    public boolean isReady;
    public int planeType;
    public boolean isMaster;

    public ClientInRoom() {
    }

    public ClientInRoom(int id, String playerName, boolean isReady, boolean isMaster, int planeType) {
        this.id = id;
        this.playerName = playerName;
        this.isReady = isReady;
        this.isMaster = isMaster;
        this.planeType = planeType;
    }
}
