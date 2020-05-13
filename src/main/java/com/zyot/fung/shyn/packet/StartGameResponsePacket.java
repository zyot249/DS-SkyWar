package com.zyot.fung.shyn.packet;

import com.zyot.fung.shyn.common.PlayerInGame;

import java.io.Serializable;
import java.util.ArrayList;

public class StartGameResponsePacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public ArrayList<PlayerInGame> playerInGames;

    public StartGameResponsePacket(ArrayList<PlayerInGame> playerInGames) {
        this.playerInGames = new ArrayList<>();
        this.playerInGames.addAll(playerInGames);
    }
}