package com.zyot.fung.shyn.server;

import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.common.Constants;
import com.zyot.fung.shyn.common.InitGameSetupEvent;
import com.zyot.fung.shyn.common.PlayerInGame;
import com.zyot.fung.shyn.packet.*;

import java.util.ArrayList;
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
        } else if (p instanceof ReadyRequestPacket) {
            ReadyRequestPacket readyRequestPacket = (ReadyRequestPacket) p;
            handleReadyRequestPacket(readyRequestPacket, connection);
        } else if (p instanceof  StartGameRequestPacket) {
            StartGameRequestPacket startGameRequest = (StartGameRequestPacket) p;
            handleStartGameRequestPacket(startGameRequest, connection);
        } else if (p instanceof PlayerIngameActionPacket) {
            PlayerIngameActionPacket packet = (PlayerIngameActionPacket) p;
            packet.playerId = connection.id;
            handlePlayerIngameActionPacket(packet);
        } else if (p instanceof ChangeGameLevelPacket) {
            ChangeGameLevelPacket changeGameLevelPacket = (ChangeGameLevelPacket) p;
            handleChangeGameLevelPacket(changeGameLevelPacket, connection);
        }
    }

    private void handleChangeGameLevelPacket(ChangeGameLevelPacket packet, Connection connection) {
        Room.setLevel(packet.level);
        UpdateRoomInfoPacket updateRoomInfoPacket = new UpdateRoomInfoPacket(Room.clients, Room.getLevel());

        for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(updateRoomInfoPacket);
        }
    }

    private void handleReadyRequestPacket(ReadyRequestPacket packet, Connection connection) {
        if (connection.id == packet.id) {
            Room.clients.forEach(clientInRoom -> {
                if (clientInRoom.id == connection.id)
                    clientInRoom.isReady = packet.isReady;
            });
            UpdateRoomInfoPacket updateRoomInfoPacket = new UpdateRoomInfoPacket(Room.clients, Room.getLevel());
            for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                Connection c = entry.getValue();
                c.sendObject(updateRoomInfoPacket);
            }
        }
    }

    private void handleAddConnectionRequestPacket(AddConnectionRequestPacket packet, Connection connection) {
        if (Room.isInGame) {
            AddConnectionResponsePacket addConnectionResponsePacket = new AddConnectionResponsePacket(
                    -1,
                    false,
                    "Room is started!");

            connection.sendObject(addConnectionResponsePacket);
            connection.close();
        }
        if (ConnectionHandler.connections.size() <= MAX_ROOM_SIZE) {

            packet.id = connection.id;
            connection.playerName = packet.playerName;
            boolean isReady = packet.isMaster;

            ClientInRoom client = new ClientInRoom(packet.id,
                    packet.playerName,
                    isReady,
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
    }

    private void handleStartGameRequestPacket(StartGameRequestPacket packet, Connection connection) {
        // only room master can start game
        if (connection.id == Room.clients.get(0).id) {
            if (!Room.isAllClientsReady()) {
                for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Connection c = entry.getValue();
                    NotReadyWarningPacket notReadyWarningPacket = new NotReadyWarningPacket();
                    notReadyWarningPacket.message = "All player must be ready!";
                    c.sendObject(notReadyWarningPacket);
                }
            } else {
                System.out.println("Server - EventLister - Start Game");
                ArrayList<PlayerInGame> playerInGames = new ArrayList<>();
                int nPlayers = Room.clients.size();
                for (int i=0; i<nPlayers; i++) {
                    int distance = (Constants.GAME_WIDTH) / nPlayers;
                    int position = i;
                    PlayerInGame playerInGame = new PlayerInGame(33 + distance / 2 + (position*distance),
                            Constants.GAME_HEIGHT + 20,
                            Room.clients.get(i).id,
                            position,
                            Room.clients.get(i).playerName);
                    playerInGames.add(playerInGame);
                }
                for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
                    Connection c = entry.getValue();
                    c.sendObject(new StartGameResponsePacket(playerInGames));
                }

                EventBuz.getInstance().post(new InitGameSetupEvent());
            }
        }
    }

    private void handlePlayerIngameActionPacket(PlayerIngameActionPacket packet) {
        EventBuz.getInstance().post(packet);
    }
}
