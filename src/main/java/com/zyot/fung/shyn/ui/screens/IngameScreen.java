package com.zyot.fung.shyn.ui.screens;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.client.Player;
import com.zyot.fung.shyn.common.*;
import com.zyot.fung.shyn.packet.PlayerIngameActionPacket;
import com.zyot.fung.shyn.packet.StartGameResponsePacket;
import com.zyot.fung.shyn.packet.UpdateIngameInfoPacket;
import com.zyot.fung.shyn.ui.LoadImage;
import com.zyot.fung.shyn.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;

public class IngameScreen extends JPanel implements ActionListener, KeyListener {
    private ArrayList<PlayerInGame> playerInGames;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    public static Canvas canvas;
    private BufferStrategy buffer;
    private Graphics g;

    private Player player;

    public IngameScreen(int width, int height, HashMap<String, Object> args) {
        initPlayer();

        setSize(width, height);
//        setLayout(null);
        setVisible(true);

        initObjectList();

        renderCanvas();

        EventBuz.getInstance().register(this);

        ScreenManager.getInstance().getWindow().addKeyListener(this);
        addKeyListener(this);
    }

    private void initPlayer() {
        player = new Player(AppPreferences.HOST_IP, Constants.HOST_PORT);
        player.connect();
    }

    private void initObjectList() {
        playerInGames = new ArrayList<>();
        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private void renderCanvas() {
        canvas = new Canvas();
        canvas.setFocusable(false);
        canvas.setPreferredSize(new Dimension(Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT));
        add(canvas);
        canvas.setVisible(true);

        LoadImage.init();
//        renderUI();
    }

    private void renderUI() {
        buffer = canvas.getBufferStrategy();
        if (buffer == null) {
            canvas.createBufferStrategy(3);
            System.out.println("buffer is NULL");
            return;
        }
        g = buffer.getDrawGraphics();
        g.clearRect(0,0,Constants.IN_GAME_SCREEN_WIDTH, Constants.IN_GAME_SCREEN_HEIGHT);

        // draw
        g.drawImage(LoadImage.image, 50 ,50, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
        renderObjects(g);
        // end of draw

        buffer.show();
        g.dispose();
    }

    private void renderObjects(Graphics g) {
        for (Enemy e : enemies) {
            e.render(g);
        }

        for (PlayerInGame player : playerInGames) {
            player.render(g);
        }

        for (Bullet bullet : bullets) {
            bullet.render(g);
        }



        g.setColor(Color.BLUE);
        g.drawString(getPlayersScore(), 70, 500);
    }

    private String getPlayersScore() {
        int totalScore = playerInGames.stream().mapToInt(PlayerInGame::getScore).sum();
        return String.format("Total score: %d", totalScore);
    }

    @Subscribe
    public void onGameStartEvent(StartGameResponsePacket startGameResponsePacket) {
        renderCanvas();
    }

    @Subscribe
    public void onUpdateIngameInfoEvent(UpdateIngameInfoPacket event) {
//        System.out.println("IngameScreen - receive update game info event");

        this.playerInGames = event.playerInGames;
        this.bullets = event.bullets;
        this.enemies = event.enemies;
        renderUI();
    }

    private void exitScreen() {
        EventBuz.getInstance().unregister(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keycode = e.getKeyCode();

        System.out.println("Keycode pressed: " + keycode);
        System.out.println("Fireeeeeeeeeeeeeeeeee");

        if (keycode == KeyEvent.VK_SPACE) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.FIRE_PRESSED));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();

        System.out.println("Keycode released: " + keycode);

        if (keycode == KeyEvent.VK_SPACE) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.FIRE_RELEASED));
        }
    }
}
