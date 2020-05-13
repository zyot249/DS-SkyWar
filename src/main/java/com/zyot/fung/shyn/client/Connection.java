package com.zyot.fung.shyn.client;

public class Connection {
    public int id;
    public String playerName;

    public Connection() {
    }

    public Connection(int id) {
        this.id = id;
    }

    public Connection(int id, String playerName) {
        this.id = id;
        this.playerName = playerName;
    }
}
