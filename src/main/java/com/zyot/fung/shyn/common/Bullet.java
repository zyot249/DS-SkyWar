package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.ui.imagehandler.ImageLoader;

import java.awt.*;
import java.io.Serializable;

public class Bullet implements Serializable {
    private int x;
    private int y;
    private int speed;
    private int ownerId;

    public Bullet(int x, int y) {
       this(x, y, -1);
    }

    public Bullet(int x, int y, int ownerId) {
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.ownerId = ownerId;
    }

    public void tick() {
        if (belongToEnemy()) {
            y = y + speed;
        } else {
            y = y - speed;
        }
    }

    public void render(Graphics g) {
        if (y < Constants.INGAME_PADDING_TOP || y > Constants.INGAME_PADDING_TOP + Constants.GAME_HEIGHT) return;
//        g.setColor(Color.BLUE);
//        g.fillRect(x, y, 6, 10);
        if (!belongToEnemy()) {
            g.drawImage(ImageLoader.playerBullet, x, y, Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT, null);
        } else {
            g.drawImage(ImageLoader.enemyBullet, x, y, Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT, null);
        }
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getOwnerId() {
        return ownerId;
    }

    private boolean belongToEnemy() {
        return ownerId == -1;
    }
}
