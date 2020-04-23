package com.zyot.fung.shyn.server.main;

import java.util.HashMap;

import com.zyot.fung.shyn.server.main.model.Player;

public class ConnectionHandler {
	public static int maxConcurentConnections = 2;
	public static HashMap<Integer,Connection> connections = new HashMap<Integer,Connection>();
	public static HashMap<Integer,Player> players = new HashMap<Integer,Player>();
}
