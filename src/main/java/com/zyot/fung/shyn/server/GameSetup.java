package com.zyot.fung.shyn.server;

import com.zyot.fung.shyn.common.PlayerInGame;
import com.zyot.fung.shyn.packet.PlayerIngameActionPacket;
import com.zyot.fung.shyn.packet.UpdateIngameInfoPacket;

import java.util.Map;

public class GameSetup implements  Runnable {
    private int numberOfPlayers;

    private Thread thread;
    private boolean running;

    private GameManager manager;

    public GameSetup(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public void run() {
        manager = new GameManager(numberOfPlayers);

        manager.init();

        int fps = 30;

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
        UpdateIngameInfoPacket updateIngameInfoPacket = new UpdateIngameInfoPacket(manager.playerInGames,
                GameManager.bullets,
                GameManager.enemies);

        for(Map.Entry<Integer, Connection> entry : ConnectionHandler.connections.entrySet()) {
            Connection c = entry.getValue();
            c.sendObject(updateIngameInfoPacket);
        }
    }

    public void handlePlayerAction(PlayerIngameActionPacket action) {
        PlayerInGame player = manager.playerInGames.stream()
                .filter(playerInGame -> playerInGame.id == action.playerId)
                .findAny()
                .orElse(null);
        if (player == null) return;

        switch (action.action) {
            case FIRE_PRESSED: {
                player.fire = true;
                break;
            }
            case FIRE_RELEASED: {
                player.fire = false;
                break;
            }
            case LEFT_PRESSED: {
                player.left = true;
                break;
            }
            case LEFT_RELEASED: {
                player.left = false;
                break;
            }
            case RIGHT_PRESSED: {
                player.right = true;
                break;
            }
            case RIGHT_RELEASED: {
                player.right = false;
                break;
            }
            default: {
                System.out.println("Server - Unknown action");
            }
        }
    }
}
