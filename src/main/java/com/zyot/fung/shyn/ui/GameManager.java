package com.zyot.fung.shyn.ui;

import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class GameManager {
    private Player player;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;

    private long current;
    private long delay;
    public GameManager() {

    }

    public void init() {
        player = new Player(GameSetup.GAME_WIDTH / 2 + 50, GameSetup.GAME_HEIGHT + 20);
        player.init();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        current = System.nanoTime();
        delay = 800;
    }

    public void tick() {
        player.tick();
        for (Bullet bullet : bullets) {
            bullet.tick();
        }

        long breaks = (System.nanoTime() - current)/1000000;
        if (breaks > delay) {
            for (int i = 0; i < 2; i++) {
                Random rand = new Random();
                int randX = rand.nextInt(450);
                int randY = rand.nextInt(450);
                enemies.add(new Enemy(randX, -randY));
            }
            current = System.nanoTime();
        }


        for (int i = 0; i < enemies.size(); i++) {
            enemies.get(i).tick();
        }
    }

    public void render(Graphics g) {
        player.render(g);
        for (Bullet bullet : bullets) {
            bullet.render(g);
        }

        for (int i = 0; i < bullets.size(); i++) {
            if (bullets.get(i).getY() <= 50) {
                bullets.remove(i);
                i--;
            }
        }

        for (Enemy e : enemies) {
            if (e.getX() >= 50 && e.getX() <= 450 - 25 && e.getY() <= 450 - 25 && e.getY() >= 50) {
                e.render(g);
            }
        }

        for (int i = 0; i < enemies.size(); i++) {
            Enemy e = enemies.get(i);
            if (isCollision(player, e)) {
                enemies.remove(i);
                i--;
                player.setHealth(player.getHealth() - 1);
                System.out.println(player.getHealth());
                if (player.getHealth() <= 0) {
                    System.out.println("Loss");
                    enemies.clear();
                }
            }

            for (int j = 0; j < bullets.size(); j++) {
                Bullet b = bullets.get(j);
                if (isCollision(e, b)) {
                    enemies.remove(i);
                    i--;
                    bullets.remove(j);
                    j--;
                    player.incScore();
                }
            }
        }

        g.setColor(Color.BLUE);
        g.drawString("Score: " + player.getScore(), 70, 500);
    }

    private boolean isCollision(Enemy e, Bullet b) {
        return e.getX() < b.getX() + 6 &&
                e.getX() + 25 > b.getX() &&
                e.getY() < b.getY() + 6 &&
                e.getY() + 25 > b.getY();
    }

    private boolean isCollision(Player p, Enemy e) {
        return p.getX() < e.getX() + 25 &&
                p.getX() + 30 > e.getX() &&
                p.getY() < e.getY() + 25 &&
                p.getY() + 30 > e.getY();
    }
}
