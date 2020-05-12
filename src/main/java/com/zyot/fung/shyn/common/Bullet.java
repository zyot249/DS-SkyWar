package com.zyot.fung.shyn.common;

import java.awt.*;
import java.io.Serializable;

public class Bullet implements Serializable {
    private int x;
    private int y;
    private int speed;

    public Bullet(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 10;
    }

    public void tick() {
        y = y - speed;
    }

    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 6, 10);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }
}
