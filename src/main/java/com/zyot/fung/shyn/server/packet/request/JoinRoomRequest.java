package com.zyot.fung.shyn.server.packet.request;

import java.io.Serializable;

import com.zyot.fung.shyn.server.packet.Request;

public class JoinRoomRequest extends Request implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int id;
	public String playerName;
	public Boolean isMaster;
	
	public JoinRoomRequest(int id, String playerName, Boolean isMaster) {
		super();
		this.id = id;
		this.playerName = playerName;
		this.isMaster = isMaster;
	}
	
}
