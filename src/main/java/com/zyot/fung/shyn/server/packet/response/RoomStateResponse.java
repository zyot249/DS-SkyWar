package com.zyot.fung.shyn.server.packet.response;

import java.io.Serializable;
import java.util.List;

import com.zyot.fung.shyn.server.main.model.Player;

public class RoomStateResponse implements Serializable{
	public List<Player> playerList;

	public RoomStateResponse(List<Player> playerList) {
		super();
		this.playerList = playerList;
	}
	
}
