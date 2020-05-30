package com.zyot.fung.shyn.server;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.common.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class GameManager {
    private int nPlayer;
    public ArrayList<PlayerInGame> players;
    public ArrayList<PlayerInGame> playerInGames;
    public static ArrayList<Bullet> bullets;
    public static ArrayList<Bullet> enemyBullets;
    public static ArrayList<Enemy> enemies;

    private long current;
    private long delay;
    public GameManager(int numberOfPlayers) {
        this.nPlayer = numberOfPlayers;

        EventBuz.getInstance().register(this);
    }

    public void init() {
        players = new ArrayList<>();
        playerInGames = new ArrayList<>();
        for (int i=0; i<nPlayer; i++) {
            int distance = (Constants.GAME_WIDTH) / nPlayer;
            int position = i;
            PlayerInGame playerInGame = new PlayerInGame(33 + distance / 2 + (position*distance),
                    Constants.GAME_HEIGHT + Constants.INGAME_PADDING_TOP - Constants.PLAYER_HEIGHT,
                    Room.clients.get(i).id,
                    position,
                    Room.clients.get(i).playerName);
            playerInGame.init();
            players.add(playerInGame);
            playerInGames.add(playerInGame);
        }

        bullets = new ArrayList<>();
        enemyBullets = new ArrayList<>();
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
        for (Bullet bullet : enemyBullets) {
            bullet.tick();
        }

        long breaks = (System.nanoTime() - current)/1000000;
        if (breaks > delay) {
            for (int i = 0; i < 2; i++) {
                Random rand = new Random();
//                int randX = rand.nextInt(Constants.INGAME_PADDING_START + Constants.GAME_WIDTH);
                int randX = ThreadLocalRandom.current().nextInt(Constants.INGAME_PADDING_START, Constants.INGAME_PADDING_START + Constants.GAME_WIDTH - Constants.ENEMY_WIDTH);
                int randY = rand.nextInt(Constants.INGAME_PADDING_TOP + Constants.GAME_HEIGHT);
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
        return e.getX() < b.getX() + Constants.BULLET_WIDTH &&
                e.getX() + Constants.ENEMY_WIDTH > b.getX() &&
                e.getY() < b.getY() + Constants.BULLET_HEIGHT &&
                e.getY() + Constants.ENEMY_HEIGHT > b.getY();
    }

    private boolean isCollision(PlayerInGame p, Enemy e) {
        return p.getX() < e.getX() + Constants.ENEMY_WIDTH &&
                p.getX() + Constants.PLAYER_WIDTH > e.getX() &&
                p.getY() < e.getY() + Constants.ENEMY_HEIGHT &&
                p.getY() + Constants.PLAYER_HEIGHT > e.getY();
    }

    private boolean isCollision(PlayerInGame p, Bullet enemyBullet) {
        return p.getX() < enemyBullet.getX() + Constants.BULLET_WIDTH &&
                p.getX() + Constants.PLAYER_WIDTH > enemyBullet.getX() &&
                p.getY() < enemyBullet.getY() + Constants.BULLET_HEIGHT &&
                p.getY() + Constants.PLAYER_HEIGHT > enemyBullet.getY();
    }

    private void removeObjects() {
        bullets = bullets.stream().filter(bullet -> bullet.getY() > Constants.INGAME_PADDING_TOP && bullet.getY() < Constants.INGAME_PADDING_TOP + Constants.GAME_HEIGHT).collect(Collectors.toCollection(ArrayList::new));
        enemyBullets = enemyBullets.stream().filter(bullet -> bullet.getY() > Constants.INGAME_PADDING_TOP && bullet.getY() < Constants.INGAME_PADDING_TOP + Constants.GAME_HEIGHT).collect(Collectors.toCollection(ArrayList::new));

        Set<PlayerInGame> playerInGameSet = new HashSet<>();
        Set<Enemy> enemySet = new HashSet<>();
        Set<Bullet> bulletSet = new HashSet<>();
        Set<Bullet> enemyBulletSet = new HashSet<>();

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
            playerInGameSet.clear();
            if (playerInGames.isEmpty()) {
               break;
            }

            for (Bullet b : bullets) {
                if (isCollision(e, b)) {
                    enemySet.add(e);
                    bulletSet.add(b);
                    playerInGames.stream()
                            .filter(playerInGame -> playerInGame.id == b.getOwnerId())
                            .findAny().ifPresent(PlayerInGame::incScore);
                }
            }
        }

        for (Bullet eBullet: enemyBullets) {
            for (PlayerInGame playerInGame : playerInGames) {
                if (isCollision(playerInGame, eBullet)) {
                    enemyBulletSet.add(eBullet);
                    playerInGame.setHealth(playerInGame.getHealth() - 1);
                    System.out.println("P" + playerInGame.getPosition() + " : " + playerInGame.getHealth());
                    if (playerInGame.getHealth() <= 0) {
                        System.out.println("P" + playerInGame.getPosition() + " : " + "Died");
                        playerInGameSet.add(playerInGame);
                    }
                }
            }
        }


        if (playerInGames.isEmpty()) {
//            enemies.clear();
            System.out.println("Loss");
        }

        playerInGames.removeAll(playerInGameSet);
        enemies.removeAll(enemySet);
        bullets.removeAll(bulletSet);
        enemyBullets.removeAll(enemyBulletSet);

    }

    @Subscribe
    protected void handlePlayerLeftEvent(PlayerLeftEvent event) {
        this.playerInGames.removeIf(player -> player.id == event.playerId);
    }

    public void doBeforeClose() {
        try {
            EventBuz.getInstance().unregister(this);
        } catch (Exception e) {

        }
    }
}
