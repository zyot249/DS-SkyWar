package com.zyot.fung.shyn.server;

import java.util.ArrayList;

public class Room extends Server{
    public static ArrayList<ClientInRoom> clients;
    private static int level;

    public Room(int port) {
        super(port);
        clients = new ArrayList<>(4);
        Room.level = 1;
    }

    public static int getLevel() {
        return level;
    }

    public static void setLevel(int level) {
        if (level < 1) {
            System.out.println("Game level must be greater than 0!");
        } else {
            Room.level = level;
        }
    }
}
