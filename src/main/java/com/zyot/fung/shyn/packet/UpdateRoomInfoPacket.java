package com.zyot.fung.shyn.packet;

import com.zyot.fung.shyn.server.ClientInRoom;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateRoomInfoPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<ClientInRoom> clients;
    public int level;

    public UpdateRoomInfoPacket() {
    }

    public UpdateRoomInfoPacket(ArrayList<ClientInRoom> clients, int level) {
        this.clients = new ArrayList<>();
        this.clients.addAll(clients);
        this.level = level;
    }
}
