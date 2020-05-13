package com.zyot.fung.shyn.server;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.common.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

public class GameManager {
    private int nPlayer;
    public ArrayList<PlayerInGame> playerInGames;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Enemy> enemies;

    private long current;
    private long delay;
    public GameManager(int numberOfPlayers) {
        this.nPlayer = numberOfPlayers;

        EventBuz.getInstance().register(this);
    }

    public void init() {
        playerInGames = new ArrayList<>();
        for (int i=0; i<nPlayer; i++) {
            int distance = (Constants.GAME_WIDTH) / nPlayer;
            int position = i;
            PlayerInGame playerInGame = new PlayerInGame(33 + distance / 2 + (position*distance),
                    Constants.GAME_HEIGHT + 20,
                    Room.clients.get(i).id,
                    position);
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

    private void removeObjects() {
        bullets = bullets.stream().filter(bullet -> bullet.getY() > 50).collect(Collectors.toCollection(ArrayList::new));

        Set<PlayerInGame> playerInGameSet = new HashSet<>();
        Set<Enemy> enemySet = new HashSet<>();
        Set<Bullet> bulletSet = new HashSet<>();

        for (Enemy e :enemies) {
            for (PlayerInGame playerInGame : playerInGames) {
                if (isCollision(playerInGame, e)) {
                    enemySet.add(e);
                    playerInGame.setHealth(playerInGame.getHealth() - 1);
                    System.out.println("P" + playerInGame.getPosition() + " : " + playerInGame.getHealth());
                    if (playerInGame.getHealth() <= 0) {
                        System.out.println("P" + playerInGame.getPosition() + " : " + "Died");
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
                    enemySet.add(e);
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

    @Subscribe
    protected void handlePlayerLeftEvent(PlayerLeftEvent event) {
        this.playerInGames.removeIf(player -> player.id == event.playerId);
    }

    @Override
    protected void finalize() throws Throwable {
        EventBuz.getInstance().unregister(this);
        super.finalize();
    }
}
