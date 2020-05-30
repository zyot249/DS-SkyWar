package com.zyot.fung.shyn.ui.screens;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.client.Player;
import com.zyot.fung.shyn.common.*;
import com.zyot.fung.shyn.packet.*;
import com.zyot.fung.shyn.ui.*;
import org.checkerframework.checker.units.qual.A;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import static com.zyot.fung.shyn.common.Constants.HOME_SCREEN;

public class IngameScreen extends JPanel implements ActionListener, KeyListener {
    private ArrayList<PlayerInGame> playerInGames;
    private ArrayList<Bullet> bullets;
    private ArrayList<Bullet> enemyBullets;
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
        enemyBullets = new ArrayList<>();
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
        PlaneImageLoader.init();
        HelicopterImageLoader.init();
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
        g.drawImage(ImageLoader.image, Constants.INGAME_PADDING_START ,Constants.INGAME_PADDING_TOP, Constants.GAME_WIDTH, Constants.GAME_HEIGHT, null);
        renderObjects(g);
        // end of draw

        buffer.show();
        g.dispose();
    }

    private void renderObjects(Graphics g) {
        for (Enemy e : enemies) {
            if (e.getX() >= Constants.INGAME_PADDING_START && e.getX() <= (Constants.INGAME_PADDING_START + Constants.GAME_WIDTH - Constants.ENEMY_WIDTH) && e.getY() <= (Constants.INGAME_PADDING_TOP + Constants.GAME_HEIGHT - Constants.ENEMY_WIDTH) && e.getY() >= Constants.INGAME_PADDING_TOP) {
                e.render(g);
            }
        }

        for (PlayerInGame player : playerInGames) {
            player.render(g);
        }

        for (Bullet bullet : bullets) {
            bullet.render(g);
        }

        for (Bullet bullet : enemyBullets) {
            bullet.render(g);
        }

        g.setColor(Color.BLUE);
//        g.drawString(getPlayersScore(), Constants.GAME_WIDTH - 200, Constants.INGAME_PADDING_TOP + 10);
        drawPlayerScores(g, getPlayersScore(), Constants.GAME_WIDTH - 200, Constants.INGAME_PADDING_TOP + 10);
    }

    private void drawPlayerScores(Graphics g, String scoreStr, int x, int y) {
        int lineHeight = 20;
        for (String line : scoreStr.split("\n"))
            g.drawString(line, x, y += lineHeight);
    }

    private String getPlayersScore() {
//        int totalScore = playerInGames.stream().mapToInt(PlayerInGame::getScore).sum();
//        return String.format("Total score: %d", totalScore);

        StringBuilder sb = new StringBuilder();
        playerInGames.forEach(player -> {
            sb.append(player.getName())
                .append(" : ")
                .append(player.getScore())
                .append("\n");

        });
        return sb.toString();
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
        this.enemyBullets.clear();
        this.enemies.clear();

        this.playerInGames.addAll(event.playerInGames);
        this.bullets.addAll(event.bullets);
        this.enemyBullets.addAll(event.enemyBullets);
        this.enemies.addAll(event.enemies);
        renderUI();
    }

    @Subscribe
    public void onGameOverEvent(GameOverPacket event) {
        event.playerInGames.sort((player1, player2) -> player2.getScore() - player1.getScore());    // sort in Descending order
        List<JComponent> playerScores = event.playerInGames.stream()
                .map(this::getItemPlayerScore)
                .collect(Collectors.toCollection(ArrayList::new));

        String header = "<html><span style='font-size:20px'>"+String.format("%-25s :    %7s", "Player","Score")+"</span></html>";
        playerScores.add(0, new JLabel(header));

        JFrame jFrameParent = ScreenManager.getInstance().getWindow();
        System.out.println(jFrameParent);
        GameOverDialog gameOverDialog = new GameOverDialog(jFrameParent, playerScores, this::backToLobby);
        gameOverDialog.showDialog();
    }

    private JComponent getItemPlayerScore(PlayerInGame player) {
        String result = "<html><span style='font-size:20px'>"+String.format("%-25s :    %7d", player.getName(), player.getScore())+"</span></html>";
        return new JLabel(result);
    }

    @Subscribe
    public void onClosedServerEvent(ClosedServerNotificationPacket event) {
        backToHome();
    }

    private void backToLobby() {
        doBeforeClose();
        ScreenManager.getInstance().navigate(Constants.EXISTED_ROOM_SCREEN);
    }

    private void backToHome() {
        doBeforeClose();
        ScreenManager.getInstance().navigate(HOME_SCREEN);
        System.gc();
    }

    private void doBeforeClose() {
        ScreenManager.getInstance().getWindow().removeKeyListener(this);
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
        } else if (keycode == KeyEvent.VK_UP) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.UP_PRESSED));
        } else if (keycode == KeyEvent.VK_DOWN) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.DOWN_PRESSED));
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
        } else if (keycode == KeyEvent.VK_UP) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.UP_RELEASED));
        } else if (keycode == KeyEvent.VK_DOWN) {
            player.sendObject(new PlayerIngameActionPacket(PlayerIngameActionPacket.Action.DOWN_RELEASED));
        }
    }

}
