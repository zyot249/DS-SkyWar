package com.zyot.fung.shyn.packet;

import com.zyot.fung.shyn.common.Bullet;
import com.zyot.fung.shyn.common.Enemy;
import com.zyot.fung.shyn.common.PlayerInGame;

import java.io.Serializable;
import java.util.ArrayList;

public class UpdateIngameInfoPacket implements Serializable {
    public ArrayList<PlayerInGame> playerInGames;
    public ArrayList<Bullet> bullets;
    public ArrayList<Bullet> enemyBullets;
    public ArrayList<Enemy> enemies;

    public UpdateIngameInfoPacket(ArrayList<PlayerInGame> playerInGames, ArrayList<Bullet> bullets, ArrayList<Bullet> enemyBullets, ArrayList<Enemy> enemies) {
        this.playerInGames = new ArrayList<>();
        this.bullets = new ArrayList<>();
        this.enemies = new ArrayList<>();
        this.enemyBullets = new ArrayList<>();

        this.playerInGames.addAll(playerInGames);
        this.bullets.addAll(bullets);
        this.enemies.addAll(enemies);
        this.enemyBullets.addAll(enemyBullets);
    }
}
