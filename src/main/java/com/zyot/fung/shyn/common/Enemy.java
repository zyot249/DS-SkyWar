package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.server.GameManager;
import com.zyot.fung.shyn.ui.ImageLoader;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class Enemy implements Serializable {
    private int x;
    private int y;

    private int speed;
    private long delay;
    private long current;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 1;
        this.delay = 1000;
        this.current = System.nanoTime();
    }

    public void tick() {
        y = y + speed;

        long breaks = (System.nanoTime() - current) / 1000000;
        int random = new Random().nextInt(100);
        if (breaks > delay && random >= 90) {
            GameManager.enemyBullets.add(new Bullet(x + (Constants.ENEMY_WIDTH-Constants.BULLET_WIDTH)/2, y + 10));
            current = System.nanoTime();
        }
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
