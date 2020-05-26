package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.ui.ImageLoader;

import java.awt.*;
import java.io.Serializable;

public class Bullet implements Serializable {
    private int x;
    private int y;
    private int speed;
    private int ownerId;

    public Bullet(int x, int y, int ownerId) {
        this.x = x;
        this.y = y;
        this.speed = 10;
        this.ownerId = ownerId;
    }

    public void tick() {
        y = y - speed;
    }

    public void render(Graphics g) {
        if (y < Constants.INGAME_PADDING_TOP) return;
//        g.setColor(Color.BLUE);
//        g.fillRect(x, y, 6, 10);
        g.drawImage(ImageLoader.bullet, x, y, Constants.BULLET_WIDTH, Constants.BULLET_HEIGHT, null);
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
}
