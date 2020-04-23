package com.zyot.fung.shyn.client.main;

import com.zyot.fung.shyn.client.packet.AddConnectionPacket;
import com.zyot.fung.shyn.client.packet.RemoveConnectionPacket;
import com.zyot.fung.shyn.server.main.model.Player;
import com.zyot.fung.shyn.server.packet.response.JoinRoomResponse;
import com.zyot.fung.shyn.server.packet.response.RoomStateResponse;

public class EventListener {
	
	public void received(Object p) {
		System.out.println("Client EventListener: "+ p.getClass());
		
		if(p instanceof AddConnectionPacket) {
			AddConnectionPacket packet = (AddConnectionPacket)p;
			ConnectionHandler.connections.put(packet.id,new Connection(packet.id));
			System.out.println(packet.id + " has connected");
		}else if(p instanceof RemoveConnectionPacket) {
			RemoveConnectionPacket packet = (RemoveConnectionPacket)p;
			System.out.println("Connection: " + packet.id + " has disconnected");
			ConnectionHandler.connections.remove(packet.id);
		} else if (p instanceof JoinRoomResponse) {
			JoinRoomResponse response = (JoinRoomResponse) p;
			if (response.success) {
				System.out.println("Ket noi thanh cong - " + response.message);
				System.out.println("my ID: " + response.clientId);
				System.out.println("current number of players: "+ response.playerList.size());
				for (Player player : response.playerList) {
					System.out.println(String.format("\t id: %d, name: %s", player.id, player.name) + (player.isMaster ? " - master": ""));
				}
			} else {
				System.out.println("Ket noi that bai - " + response.message);
			}
		} else if (p instanceof RoomStateResponse) {
			RoomStateResponse response = (RoomStateResponse) p;
			System.out.println("Update players in room: ");
			for (Player player : response.playerList) {
				System.out.println(String.format("\t id: %d, name: %s, ready: %s", player.id, player.name, player.ready) + (player.isMaster ? " - master": ""));
			}
		} else {
			System.out.println("Client - Cannot identify the request");
		}
	}

}
