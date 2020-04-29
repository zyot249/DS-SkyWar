package com.zyot.fung.shyn.packet;

import java.io.Serializable;

public class AddConnectionPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;

    public String playerName;

    public AddConnectionPacket() {
    }

    public AddConnectionPacket(String playerName) {
        this.playerName = playerName;
    }

    public AddConnectionPacket(int id, String playerName) {
        this.id = id;
        this.playerName = playerName;
    }
}
