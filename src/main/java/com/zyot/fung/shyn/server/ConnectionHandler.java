package com.zyot.fung.shyn.server;

import java.util.HashMap;

public class ConnectionHandler {
    public static final int MAX_SIZE = 4;

    public static HashMap<Integer, Connection> connections = new HashMap<>();
}
