package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.ui.ImageLoader;

import java.awt.*;
import java.io.Serializable;

public class Enemy implements Serializable {
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
