package com.zyot.fung.shyn.server;

import com.zyot.fung.shyn.common.PlayerInGame;
import com.zyot.fung.shyn.packet.PlayerIngameActionPacket;
import com.zyot.fung.shyn.packet.UpdateIngameInfoPacket;

import java.util.Map;

public class GameSetup implements  Runnable {
    int numberOfPlayers = 0;

    private Thread thread;
    private boolean running;

    private GameManager manager;

    public GameSetup(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void run() {
        manager = new GameManager(numberOfPlayers);

        manager.init();

        int fps = 120;

        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long current = System.nanoTime();

        while (running) {
            delta = delta + (System.nanoTime() - current) / timePerTick;
            current = System.nanoTime();
            if (delta >= 1) {
                tick();
                delta--;
            }
        }
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
        manager.tick();
        sendNewIngameStateToClients();
    }

    private void sendNewIngameStateToClients() {
        for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
            Connection c = entry.getValue();

            UpdateIngameInfoPacket updateIngameInfoPacket = new UpdateIngameInfoPacket(manager.playerInGames, GameManager.bullets, GameManager.enemies);

            c.sendObject(updateIngameInfoPacket);
        }
    }

    public void handlePlayerAction(PlayerIngameActionPacket action) {
        switch (action.action) {
            case FIRE_PRESSED: {
                PlayerInGame player = manager.playerInGames.stream()
                        .filter(playerInGame -> playerInGame.id == action.playerId)
                        .findAny()
                        .orElse(null);
                if (player != null) {
                    System.out.println("Server - player " + action.playerId + " - IS firing");
                    player.fire = true;
                }
                break;
            }
            case LEFT_RELEASED: {
                manager.playerInGames.stream()
                        .filter(playerInGame -> playerInGame.id == action.playerId)
                        .findAny().ifPresent(player -> {
                            System.out.println("Server - player " + action.playerId + " - STOP firing");
                            player.fire = false;
                        });
                break;
            }
        }
    }
}
