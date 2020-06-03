package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.server.GameManager;
import com.zyot.fung.shyn.ui.imagehandler.ImageLoader;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.Serializable;

public class PlayerInGame implements Serializable {
    private int position;
    public int id;
    private String name;
    public int planeType;

    private int x;
    private int y;
    public boolean fire;
    public boolean right;
    public boolean left;
    public boolean up;
    public boolean down;

    private long current;
    private long delay;
    private int health;
    private int score;

    private int speed = 4;

    public PlayerInGame(int x, int y, int id, int position) {
        this(x,y,id,position,"Player Unknown", 0);
    }

    public PlayerInGame(int x, int y, int id, int position, String playerName, int planeType) {
        this.id = id;
        this.position = position;
        this.x = x;
        this.y = y;
        this.name = playerName;
        this.planeType = planeType;
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
                if (x >= Constants.INGAME_PADDING_START) {
                    x = x - speed;
                }
            }
            if (right) {
                if (x <= Constants.INGAME_PADDING_START + Constants.GAME_WIDTH - Constants.PLAYER_WIDTH) {
                    x = x + speed;
                }
            }
            if (up) {
                if (y > Constants.INGAME_PADDING_TOP) {
                    y = y - speed;
                }
            }
            if (down) {
                if (y < Constants.INGAME_PADDING_TOP + Constants.GAME_HEIGHT - Constants.PLAYER_WIDTH) {
                    y = y + speed;
                }
            }
            if (fire) {
                long breaks = (System.nanoTime() - current) / 1000000;
                if (breaks > delay) {
                    GameManager.bullets.add(new Bullet(x + (Constants.PLAYER_WIDTH-Constants.BULLET_WIDTH)/2, y + 10, this.id));
                    current = System.nanoTime();
                }
            }
        }
    }

    public void render(Graphics g, BufferedImage frame) {
        if (health > 0) {
            g.drawImage(frame, x, y, Constants.PLAYER_WIDTH, Constants.PLAYER_HEIGHT, null);
            if (isMe())
                g.drawImage(ImageLoader.loadImage("/arrow_up.png"), x + 23, y + 55, 5, 5, null);
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

    public String getName() {
        return name;
    }
}
