package com.zyot.fung.shyn.server;

import com.zyot.fung.shyn.packet.AddConnectionRequestPacket;
import com.zyot.fung.shyn.packet.AddConnectionResponsePacket;
import com.zyot.fung.shyn.packet.RemoveConnectionPacket;
import com.zyot.fung.shyn.packet.UpdateRoomInfoPacket;

import java.util.Map;

import static com.zyot.fung.shyn.common.Constants.MAX_ROOM_SIZE;

public class EventListener {
    public void received(Object p, Connection connection) {
        if (p instanceof AddConnectionRequestPacket) {
            AddConnectionRequestPacket addConnectionRequestPacket = (AddConnectionRequestPacket) p;
            handleAddConnectionRequestPacket(addConnectionRequestPacket, connection);
        } else if (p instanceof RemoveConnectionPacket) {
            RemoveConnectionPacket removeConnectionPacket = (RemoveConnectionPacket) p;
            handleRemoveConnectionPacket(removeConnectionPacket, connection);
        }
    }

    private void handleAddConnectionRequestPacket(AddConnectionRequestPacket packet, Connection connection) {
        if (ConnectionHandler.connections.size() <= MAX_ROOM_SIZE) {

            packet.id = connection.id;
            connection.playerName = packet.playerName;

            ClientInRoom client = new ClientInRoom(packet.id,
                    packet.playerName,
                    false,
                    packet.isMaster);
            Room.clients.add(client);
            UpdateRoomInfoPacket updateRoomInfoPacket = new UpdateRoomInfoPacket(Room.clients, Room.getLevel());

            for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Connection c = entry.getValue();
                if (c != connection) {
                    c.sendObject(packet);
                } else {
                    AddConnectionResponsePacket addConnectionResponsePacket = new AddConnectionResponsePacket(
                            connection.id,
                            true,
                            packet.playerName,
                            "Connect successfully to server!");

                    c.sendObject(addConnectionResponsePacket);
                }
                c.sendObject(updateRoomInfoPacket);
            }
            System.out.println("Client " + packet.id + " with name " + packet.playerName + " is connected!");
        } else {
            AddConnectionResponsePacket addConnectionResponsePacket = new AddConnectionResponsePacket(
                    -1,
                    false,
                    "Room is full!");

            connection.sendObject(addConnectionResponsePacket);
            connection.close();
        }
    }

    private void handleRemoveConnectionPacket(RemoveConnectionPacket packet, Connection connection) {
        System.out.println("Client: " + packet.id + " with name " + packet.playerName + " has disconnected");
        ConnectionHandler.connections.get(packet.id).close();
//        for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
//            Connection c = entry.getValue();
//            if (c.id != connection.id)
//                c.sendObject(packet);
//        }
    }
}
