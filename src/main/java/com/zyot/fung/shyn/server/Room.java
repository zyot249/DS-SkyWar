package com.zyot.fung.shyn.server;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.common.InitGameSetupEvent;
import com.zyot.fung.shyn.packet.PlayerIngameActionPacket;

import java.util.ArrayList;

public class Room extends Server{
    public static ArrayList<ClientInRoom> clients;
    public static GameSetup game;
    private static int level;

    public Room(int port) {
        super(port);
        clients = new ArrayList<>(4);

        EventBuz.getInstance().register(this);
        Room.level = 0;
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

    @Subscribe
    public void onGameStartEvent(InitGameSetupEvent initEvent) {
        game = new GameSetup(clients.size());
        game.start();
    }

    @Subscribe
    public void onPlayerActionEvent(PlayerIngameActionPacket event) {
        game.handlePlayerAction(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        EventBuz.getInstance().unregister(this);
    }
}
