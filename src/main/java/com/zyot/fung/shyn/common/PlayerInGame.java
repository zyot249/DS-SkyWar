package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.server.GameManager;
import com.zyot.fung.shyn.ui.ImageLoader;

import java.awt.*;
import java.io.Serializable;

public class PlayerInGame implements Serializable {
    private int position;
    public int id;
    private int x;
    private int y;
    public boolean fire;
    public boolean right;
    public boolean left;

    private long current;
    private long delay;
    private int health;
    private int score;

    public PlayerInGame(int x, int y, int id, int position) {
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
    }

    public void init() {
        current = System.nanoTime();
        delay = 100;
        health = 3;
        score = 0;
    }

    public void tick() {
        if (health > 0) {
            if (left) {
                if (x >= 50) {
                    x = x - 4;
                }
            }
            if (right) {
                if (x <= 450 - 30) {
                    x = x + 4;
                }
            }
            if (fire) {
                long breaks = (System.nanoTime() - current) / 1000000;
                if (breaks > delay) {
                    GameManager.bullets.add(new Bullet(x + 11, y + 10));
                    current = System.nanoTime();
                }
            }
        }
    }

    public void render(Graphics g) {
        if (health > 0) {
            if (isMe()) {
                g.drawImage(ImageLoader.myPlane, x, y, 30, 30, null);
            } else {
                g.drawImage(ImageLoader.player, x, y, 30, 30, null);
            }
        }
    }

    private boolean isMe() {
        return id == AppPreferences.UID;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getHealth() {
        return this.health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getScore() {
        return this.score;
    }

    public void incScore() {
        this.score = this.score + 1;
    }

    public int getPosition() {
        return position;
    }
}
