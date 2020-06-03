package com.zyot.fung.shyn.server;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.common.InitGameSetupEvent;
import com.zyot.fung.shyn.packet.PlayerIngameActionPacket;

import java.util.ArrayList;

public class Room extends Server{
    public static ArrayList<ClientInRoom> clients;
    public static GameSetup game;
    public static boolean isInGame;

    public Room() {
        clients = new ArrayList<>(4);

        EventBuz.getInstance().register(this);
        isInGame = false;
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

    public void doBeforeClose() {
        try {
            EventBuz.getInstance().unregister(this);
            if (game != null) {
                game.doBeforeClose();
            }
        } catch (Exception e) {

        }
    }

    public static boolean isAllClientsReady() {
        for (ClientInRoom client : clients) {
            if (!client.isReady)
                return false;
        }
        return true;
    }
}
