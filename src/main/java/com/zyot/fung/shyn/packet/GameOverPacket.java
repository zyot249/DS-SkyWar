package com.zyot.fung.shyn.packet;

import com.zyot.fung.shyn.common.PlayerInGame;

import java.io.Serializable;
import java.util.ArrayList;

public class GameOverPacket implements Serializable {
    public ArrayList<PlayerInGame> playerInGames;

    public GameOverPacket(ArrayList<PlayerInGame> playerInGames) {
        this.playerInGames = new ArrayList<>();

        this.playerInGames.addAll(playerInGames);
    }
}
