package com.zyot.fung.shyn.common;

import com.zyot.fung.shyn.server.GameManager;
import com.zyot.fung.shyn.ui.LoadImage;

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
//        if (isMe()) {
//            Display.frame.addKeyListener(this);
//        }

        current = System.nanoTime();
        delay = 100;
        health = 3;
        score = 0;
    }

    public void tick() {
/*        if (!isMe() && health > 0) {
            Random rand =  new Random();
            int movement = rand.nextInt(30); // 0-not moving , 1-left, 2-right
            int firing = rand.nextInt(10);   // 0-fire , other- not fire

            if (movement<10){
                    this.left = false;
                    this.right = false;
            } else if (movement<20){
                    this.left = true;
                    this.right = false;
            } else if (movement<30) {
                    this.left = false;
                    this.right = true;
            }

            this.fire = (firing==1);
        }*/
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
                g.drawImage(LoadImage.myPlane, x, y, 30, 30, null);
            } else {
                g.drawImage(LoadImage.player, x, y, 30, 30, null);
            }
        }
    }

    private boolean isMe() {
        return id == AppPreferences.UID;
    }

 /*   @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (keycode == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (keycode == KeyEvent.VK_SPACE) {
            fire = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();
        if (keycode == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (keycode == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (keycode == KeyEvent.VK_SPACE) {
            fire = false;
        }
    }*/

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
