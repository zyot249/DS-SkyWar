package com.zyot.fung.shyn.server.main;

import java.util.ArrayList;
import java.util.List;

import com.zyot.fung.shyn.server.main.model.Player;
import com.zyot.fung.shyn.server.packet.AddConnectionPacket;
import com.zyot.fung.shyn.server.packet.RemoveConnectionPacket;
import com.zyot.fung.shyn.server.packet.request.JoinRoomRequest;
import com.zyot.fung.shyn.server.packet.response.JoinRoomResponse;
import com.zyot.fung.shyn.server.packet.response.RoomStateResponse;

public class EventListener {
	
	public void received(Object p,Connection connection) {
		System.out.println("Server EventListener: "+ p.getClass());
		
		if(p instanceof AddConnectionPacket) {
			AddConnectionPacket packet = (AddConnectionPacket)p;
			packet.id = connection.id;
			for(int i=0; i<ConnectionHandler.connections.size(); i++) {
				Connection c = ConnectionHandler.connections.get(i);
				if(c != connection) {
					c.sendObject(packet);
				}
			}
			
		}else if(p instanceof RemoveConnectionPacket) {
			RemoveConnectionPacket packet = (RemoveConnectionPacket)p;
			System.out.println("Connection: " + packet.id + " has disconnected");
			ConnectionHandler.connections.get(packet.id).close();
			ConnectionHandler.connections.remove(packet.id);
		} else if (p instanceof JoinRoomRequest) {
			if (ConnectionHandler.connections.size() > ConnectionHandler.maxConcurentConnections) {
				JoinRoomResponse response = new JoinRoomResponse(connection.id, false, "Phong da du nguoi choi", null);
				connection.sendObject(response);
				ConnectionHandler.connections.get(connection.id).close();
				ConnectionHandler.connections.remove(connection.id);
			} else {
				JoinRoomRequest request = (JoinRoomRequest)p;
				ConnectionHandler.players.put(connection.id, new Player(connection.id, request.playerName, request.isMaster));
				List<Player> currentPlayers = new ArrayList<Player>(ConnectionHandler.players.values());
				JoinRoomResponse response = new JoinRoomResponse(connection.id, true, "Ket noi thanh cong", currentPlayers);
				connection.sendObject(response);
				RoomStateResponse roomState = new RoomStateResponse(currentPlayers);
				for(Connection c :ConnectionHandler.connections.values()) {
					if(c != connection) {
						c.sendObject(roomState);
					}
				}
			}
		} else {
			System.out.println("Server - Cannot identify the request");
		}
	}

}
