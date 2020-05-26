package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.ui.ImageLoader;

import java.awt.*;
import java.io.Serializable;

public class Enemy implements Serializable {
    private int x;
    private int y;

    private int speed;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 1;
    }

    public void tick() {
        y = y + speed;
    }

    public void render(Graphics g) {
        g.drawImage(ImageLoader.enemy, x, y, Constants.ENEMY_WIDTH, Constants.ENEMY_HEIGHT, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }


    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
