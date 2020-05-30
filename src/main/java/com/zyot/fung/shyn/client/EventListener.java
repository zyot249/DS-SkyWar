package com.zyot.fung.shyn.client;

import com.zyot.fung.shyn.common.AppPreferences;
import com.zyot.fung.shyn.packet.*;

public class EventListener {
    public void received(Object p, Client client) {
        if (p instanceof AddConnectionRequestPacket) {
            AddConnectionRequestPacket addConnectionRequestPacket = (AddConnectionRequestPacket) p;
            handleAddConnectionRequestPacket(addConnectionRequestPacket, client);
        } else if (p instanceof RemoveConnectionPacket) {
            RemoveConnectionPacket removeConnectionPacket = (RemoveConnectionPacket) p;
            handleRemoveConnectionPacket(removeConnectionPacket, client);
        } else if (p instanceof AddConnectionResponsePacket) {
            AddConnectionResponsePacket addConnectionResponsePacket = (AddConnectionResponsePacket) p;
            handleAddConnectionResponsePacket(addConnectionResponsePacket, client);
        } else if (p instanceof UpdateRoomInfoPacket) {
            UpdateRoomInfoPacket updateRoomInfoPacket = (UpdateRoomInfoPacket) p;
            System.out.println("Room Size: " + updateRoomInfoPacket.clients.size());
            handleUpdateRoomInfoPacket(updateRoomInfoPacket, client);
        } else if (p instanceof ClosedServerNotificationPacket) {
            ClosedServerNotificationPacket closedServerNotificationPacket = (ClosedServerNotificationPacket) p;
            handleCloseServerNotificationPacket(closedServerNotificationPacket);
        } else if (p instanceof StartGameResponsePacket) {
            StartGameResponsePacket startGameResponse = (StartGameResponsePacket) p;
            handleStartGameResponsePacket(startGameResponse);
        } else if (p instanceof UpdateIngameInfoPacket) {
            UpdateIngameInfoPacket packet = (UpdateIngameInfoPacket) p;
            handleUpdateIngameInfoPacket(packet);
        } else if (p instanceof NotReadyWarningPacket) {
            NotReadyWarningPacket packet = (NotReadyWarningPacket) p;
            handleNotReadyWarningPacket(packet);
        } else if (p instanceof GameOverPacket) {
            GameOverPacket packet = (GameOverPacket) p;
            handleGameOverPacket(packet);
        }
    }

    private void handleNotReadyWarningPacket(NotReadyWarningPacket packet) {
        EventBuz.getInstance().post(packet);
    }

    private void handleUpdateRoomInfoPacket(UpdateRoomInfoPacket updateRoomInfoPacket, Client client) {
        System.out.println("--------------------Room Info--------------------");
        System.out.println("Game level: " + updateRoomInfoPacket.level);
        updateRoomInfoPacket.clients.forEach(clientInRoom -> {
                    System.out.println(new StringBuilder()
                            .append("Position: ").append(updateRoomInfoPacket.clients.indexOf(clientInRoom)).append("\n")
                            .append("ID: ").append(clientInRoom.id).append("\n")
                            .append("PlayerInGame name: ").append(clientInRoom.playerName).append("\n")
                            .append("Ready? ").append(clientInRoom.isReady).append("\n")
                            .append("Master? ").append(clientInRoom.isMaster).append("\n")
                            .toString()
                    );
                }
        );
        EventBuz.getInstance().post(updateRoomInfoPacket);
    }

    private void handleAddConnectionRequestPacket(AddConnectionRequestPacket packet, Client client) {
        ConnectionHandler.connections.put(packet.id, new Connection(packet.id, packet.playerName));
        System.out.println("PlayerInGame " + packet.playerName + " has connected");
    }

    private void handleRemoveConnectionPacket(RemoveConnectionPacket packet, Client client) {
        System.out.println("PlayerInGame: " + packet.playerName + " has disconnected");
        ConnectionHandler.connections.remove(packet.id);
    }

    private void handleAddConnectionResponsePacket(AddConnectionResponsePacket packet, Client client) {
        System.out.println(packet.message);
        client.setId(packet.id);
        AppPreferences.UID = packet.id;

        if (packet.isConnectSuccess) {
            ConnectionHandler.connections.put(packet.id, new Connection(packet.id, packet.playerName));
        }
        EventBuz.getInstance().post(packet);
    }

    private void handleCloseServerNotificationPacket(ClosedServerNotificationPacket packet) {
        ConnectionHandler.connections.clear();
        EventBuz.getInstance().post(packet);
    }

    private void handleStartGameResponsePacket(StartGameResponsePacket packet) {
        EventBuz.getInstance().post(packet);
    }

    private void handleGameOverPacket(GameOverPacket packet) {
        EventBuz.getInstance().post(packet);
    }

    private void handleUpdateIngameInfoPacket(UpdateIngameInfoPacket packet) {
        EventBuz.getInstance().post(packet);
    }
}
