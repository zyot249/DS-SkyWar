package com.zyot.fung.shyn.server.main.model;

import java.io.Serializable;

public class Player implements Serializable{
	public int id;
	public String name;
	public int position;
	public Boolean ready = false;
	public Boolean isMaster = false;

	public Player(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Player(int id, String name, Boolean isMaster) {
		super();
		this.id = id;
		this.name = name;
		this.ready = isMaster;
		this.isMaster = isMaster;
	}
	
}
