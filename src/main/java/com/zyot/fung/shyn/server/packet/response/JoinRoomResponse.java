package com.zyot.fung.shyn.server.packet.response;

import java.io.Serializable;
import java.util.List;

import com.zyot.fung.shyn.server.main.model.Player;

public class JoinRoomResponse implements Serializable{
	public int clientId;
	public Boolean success;
	public String message;
	public List<Player> playerList;
	
	public JoinRoomResponse(int clientId, Boolean success, String message, List<Player> playerList) {
		super();
		this.clientId = clientId;
		this.success = success;
		this.message = message;
		this.playerList = playerList;
	}

}
