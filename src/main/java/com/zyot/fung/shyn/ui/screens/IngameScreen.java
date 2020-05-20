package com.zyot.fung.shyn.ui.screens;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.client.Player;
import com.zyot.fung.shyn.common.*;
import com.zyot.fung.shyn.packet.ClosedServerNotificationPacket;
import com.zyot.fung.shyn.packet.PlayerIngameActionPacket;
import com.zyot.fung.shyn.packet.StartGameResponsePacket;
import com.zyot.fung.shyn.packet.UpdateIngameInfoPacket;
import com.zyot.fung.shyn.ui.ImageLoader;
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

import static com.zyot.fung.shyn.common.Constants.HOME_SCREEN;

public class IngameScreen extends JPanel implements ActionListener, KeyListener {
    private ArrayList<PlayerInGame> playerInGames;
    private ArrayList<Bullet> bullets;
    private ArrayList<Enemy> enemies;

    public static Canvas canvas;
    private BufferStrategy buffer;
    private Graphics g;

    private Player player;

    public IngameScreen(int width, int height, HashMap<String, Object> args) {
        if (args != null && args.containsKey("player")) {
            player = (Player) args.get("player");
        } else {
            JOptionPane.showMessageDialog(this, "Player in IngameScreen is null", "No Connection", JOptionPane.WARNING_MESSAGE);
        }

        setSize(width, height);
        setVisible(true);

        initObjectList();

        renderCanvas();

        EventBuz.getInstance().register(this);

        ScreenManager.getInstance().getWindow().addKeyListener(this);
        ScreenManager.getInstance().getWindow().setFocusable(true);
    }

//    private void initPlayer(String host) {
//        player = new Player(host, Constants.HOST_PORT);
//        player.connect();
//    }

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

        ImageLoader.init();
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
        g.drawImage(ImageLoader.image, 50 ,50, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
        renderObjects(g);
        // end of draw

        buffer.show();
        g.dispose();
    }

    private void renderObjects(Graphics g) {
        for (Enemy e : enemies) {
            if (e.getX() >= 50 && e.getX() <= 450 - 25 && e.getY() <= 450 - 25 && e.getY() >= 50) {
                e.render(g);
            }
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
//        System.out.println(String.format("IngameScreen - receive update game info event: %d players - %d bullets - %d enemies", event.playerInGames.size(), event.bullets.size(), event.enemies.size()));

        this.playerInGames.clear();
        this.bullets.clear();
        this.enemies.clear();

        this.playerInGames.addAll(event.playerInGames);
        this.bullets.addAll(event.bullets);
        this.enemies.addAll(event.enemies);
        renderUI();
    }

    @Subscribe
    public void onClosedServerEvent(ClosedServerNotificationPacket event) {
        backToHome();
    }

    private void backToHome() {
//        exitScreen();
        ScreenManager.getInstance().navigate(HOME_SCREEN);
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

//        System.out.println("Keycode pressed: " + keycode);

        if (keycode == KeyEvent.VK_SPACE) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.FIRE_PRESSED));
        } else if (keycode == KeyEvent.VK_LEFT) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.LEFT_PRESSED));
        } else if (keycode == KeyEvent.VK_RIGHT) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.RIGHT_PRESSED));
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keycode = e.getKeyCode();

//        System.out.println("Keycode released: " + keycode);

        if (keycode == KeyEvent.VK_SPACE) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.FIRE_RELEASED));
        } else if (keycode == KeyEvent.VK_LEFT) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.LEFT_RELEASED));
        } else if (keycode == KeyEvent.VK_RIGHT) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.RIGHT_RELEASED));
        }
    }

    @Override
    protected void finalize() throws Throwable {
        ScreenManager.getInstance().getWindow().removeKeyListener(this);
        EventBuz.getInstance().unregister(this);
        super.finalize();
    }
}
