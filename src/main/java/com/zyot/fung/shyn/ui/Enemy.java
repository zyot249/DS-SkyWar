package com.zyot.fung.shyn.ui;

import java.awt.*;

public class Enemy {
    private int x;
    private int y;

    public Enemy(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void tick() {
        y = y + 1;
    }

    public void render(Graphics g) {
        g.drawImage(ImageLoader.enemy, x, y, 25, 25, null);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
