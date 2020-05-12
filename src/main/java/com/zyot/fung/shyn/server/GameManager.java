package com.zyot.fung.shyn.server;

import com.zyot.fung.shyn.common.Bullet;
import com.zyot.fung.shyn.common.Constants;
import com.zyot.fung.shyn.common.Enemy;
import com.zyot.fung.shyn.common.PlayerInGame;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GameManager {
    private int nPlayer;
    public ArrayList<PlayerInGame> playerInGames;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;

    private long current;
    private long delay;
    public GameManager(int numberOfPlayers) {
        this.nPlayer = numberOfPlayers;
    }

    public void init() {
        playerInGames = new ArrayList<>();
        for (int i=0; i<nPlayer; i++) {
            int distance = (Constants.GAME_WIDTH) / nPlayer;
            int position = i;
            PlayerInGame playerInGame = new PlayerInGame(33 + distance / 2 + (position*distance), Constants.GAME_HEIGHT + 20, i, position);
            playerInGame.init();
            playerInGames.add(playerInGame);
        }

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();

        current = System.nanoTime();
        delay = 800;
    }

    public void tick() {
        for (PlayerInGame playerInGame : playerInGames) {
            playerInGame.tick();
        }
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

        removeObjects();
    }

   /* public void render(Graphics g) {
        for (PlayerInGame playerInGame : playerInGames) {
            playerInGame.render(g);
        }

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

//        for (Enemy e :enemies) {
//            for (PlayerInGame player: playerInGames) {
//                if (isCollision(player, e)) {
//                    enemies.remove(e);
//                    player.setHealth(player.getHealth() - 1);
//                    System.out.println("P" + playerInGames.indexOf(player) + " : " + player.getHealth());
//                    if (player.getHealth() <= 0) {
//                        System.out.println("P" + playerInGames.indexOf(player) + " : " + "Died");
//                        playerInGames.remove(player);
//                    }
//                    if (playerInGames.isEmpty()) {
//                        enemies.clear();
//                        System.out.println("Loss");
//                    }
//                }
//            }
//
//
//            for (Bullet b : bullets) {
//                if (isCollision(e, b)) {
//                    enemies.remove(e);
//                    bullets.remove(b);
//                    playerInGames.get(0).incScore(); // TODO calculate score of each player
//                }
//            }
//        }
        removeObjects();

        g.setColor(Color.BLUE);
        g.drawString(getPlayersScore(), 70, 500);
    }*/

    private boolean isCollision(Enemy e, Bullet b) {
        return e.getX() < b.getX() + 6 &&
                e.getX() + 25 > b.getX() &&
                e.getY() < b.getY() + 6 &&
                e.getY() + 25 > b.getY();
    }

    private boolean isCollision(PlayerInGame p, Enemy e) {
        return p.getX() < e.getX() + 25 &&
                p.getX() + 30 > e.getX() &&
                p.getY() < e.getY() + 25 &&
                p.getY() + 30 > e.getY();
    }

/*    private String getPlayersScore() {
//        StringBuilder stringBuilder = new StringBuilder();
//        stringBuilder.append("Score:\n\t");
//        for (int i=0; i<=playerInGames.size(); i++) {
//            stringBuilder.append("P").append(i + 1).append(" : ")
//                    .append(playerInGames.get(i).getScore())
//                    .append("\t");
//        }
//
//        return stringBuilder.toString();

        int totalScore = playerInGames.stream().mapToInt(PlayerInGame::getScore).sum();
        return String.format("Total score: %d", totalScore);
    }*/

    private void removeObjects() {
        Set<PlayerInGame> playerInGameSet = new HashSet<>();
        Set<Enemy> enemySet = new HashSet<>();
        Set<Bullet> bulletSet = new HashSet<>();

        for (Enemy e :enemies) {
            for (PlayerInGame playerInGame : playerInGames) {
                if (isCollision(playerInGame, e)) {
//                    enemies.remove(e);
                    enemySet.add(e);
                    playerInGame.setHealth(playerInGame.getHealth() - 1);
                    System.out.println("P" + playerInGame.getPosition() + " : " + playerInGame.getHealth());
                    if (playerInGame.getHealth() <= 0) {
                        System.out.println("P" + playerInGame.getPosition() + " : " + "Died");
//                        playerInGames.remove(playerInGame);
                        playerInGameSet.add(playerInGame);
                    }
                }
            }

            playerInGames.removeAll(playerInGameSet);
            if (playerInGames.isEmpty()) {
               break;
            }

            for (Bullet b : bullets) {
                if (isCollision(e, b)) {
//                    enemies.remove(e);
                    enemySet.add(e);
//                    bullets.remove(b);
                    bulletSet.add(b);
                    playerInGames.get(0).incScore(); // TODO calculate score of each player
                }
            }
        }

        if (playerInGames.isEmpty()) {
//            enemies.clear();
            System.out.println("Loss");
        }

        enemies.removeAll(enemySet);
        bullets.removeAll(bulletSet);

    }
}
