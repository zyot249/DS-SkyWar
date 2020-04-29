package com.zyot.fung.shyn.client;

import com.zyot.fung.shyn.packet.AddConnectionPacket;
import com.zyot.fung.shyn.packet.AddConnectionResponsePacket;
import com.zyot.fung.shyn.packet.RemoveConnectionPacket;

public class EventListener {
    public void received(Object p, Client client) {
        if (p instanceof AddConnectionPacket) {
            AddConnectionPacket packet = (AddConnectionPacket)p;
            ConnectionHandler.connections.put(packet.id, new Connection(packet.id, packet.playerName));
            System.out.println("Player " + packet.playerName + " has connected");
        } else if (p instanceof RemoveConnectionPacket) {
            RemoveConnectionPacket packet = (RemoveConnectionPacket)p;
            System.out.println("Player: " + packet.playerName + " has disconnected");
            ConnectionHandler.connections.remove(packet.id);
        } else if (p instanceof AddConnectionResponsePacket) {
            AddConnectionResponsePacket packet = (AddConnectionResponsePacket) p;
            System.out.println(packet.message);
            client.setId(packet.id);

            if (!packet.isConnectSuccess) {
                client.close();
            } else {
                ConnectionHandler.connections.put(packet.id, new Connection(packet.id, packet.playerName));
            }
        }
    }
}
