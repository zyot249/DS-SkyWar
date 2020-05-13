package com.zyot.fung.shyn.ui.screens;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.client.Player;
import com.zyot.fung.shyn.packet.*;
import com.zyot.fung.shyn.server.ClientInRoom;
import com.zyot.fung.shyn.server.Room;
import com.zyot.fung.shyn.ui.PlayerHolder;
import com.zyot.fung.shyn.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import static com.zyot.fung.shyn.common.Constants.*;

public class RoomScreen extends JPanel implements ActionListener {
    private ScreenManager screenManager;
    private JButton exitBtn;
    private JButton startGameBtn;
    private JButton readyBtn;
    private JSeparator separator;
    private ArrayList<PlayerHolder> playerHolders;
    private int[] playerHolderLocations = {20, 240, 460, 680};
    private JComboBox levelSelector;

    private Vector<String> levels;
    private Room room;

    private Player player;
    public String playerName;



    public RoomScreen(int width, int height, HashMap<String, Object> args) {
        levels = new Vector<>();
        levels.add("Easy");
        levels.add("Medium");
        levels.add("Hard");
        levels.add("Super");
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(false);
        if (args != null) {
            boolean isMaster = false;
            if (args.containsKey("isRoomMaster")) {
                if ((Boolean)args.get("isRoomMaster")) {
                    initRoomServer();
                    renderUIofMaster();
                    isMaster = (boolean) args.get("isRoomMaster");
                }
            }

            if (args.containsKey("playerName")) {
                this.playerName = args.get("playerName").toString();
                initPlayer(isMaster);
            }
        }
    }

    private void renderUIofMaster() {
        startGameBtn = new JButton("Start Game");
        startGameBtn.setBounds(660, 540, 220, 50);
        startGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        startGameBtn.addActionListener(this);

        levelSelector.setEnabled(true);

        add(startGameBtn);
        remove(readyBtn);
    }

    private void initRoomServer() {
        room = new Room(HOST_PORT);
        room.start();
    }

    private void initPlayer(boolean isMaster) {
        player = new Player("localhost", HOST_PORT);
        player.isReady = isMaster;
        player.playerName = this.playerName;
        player.connect();
        try {
            AddConnectionRequestPacket addConnectionRequestPacket = new AddConnectionRequestPacket(playerName, isMaster);
            player.sendObject(addConnectionRequestPacket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        exitBtn = new JButton("Exit Room");
        readyBtn = new JButton("Ready");
        separator = new JSeparator();
        playerHolders = new ArrayList<>();
        for (int i = 0; i < playerHolderLocations.length; i++) {
            playerHolders.add(new PlayerHolder(playerHolderLocations[i], 210));
            add(playerHolders.get(i));
        }

        levelSelector = new JComboBox(levels);
        levelSelector.setBounds(730, 10, 150, 25);
        levelSelector.setSelectedIndex(0);
        levelSelector.setEditable(false);
        levelSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String levelName = e.getItem().toString();
                requestChangeGameLevel(levelName);
            }
        });
        levelSelector.setEnabled(false);

        exitBtn.setBounds(20, 10, 110, 25);
        exitBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));
        readyBtn.setBounds(660, 540, 220, 50);
        readyBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        separator.setBounds(20, 525, 860, 10);

        exitBtn.addActionListener(this);
        readyBtn.addActionListener(this);

        add(levelSelector);
        add(exitBtn);
        add(readyBtn);
        add(separator);

        EventBuz.getInstance().register(this);
    }

    @Subscribe
    public void onReceiveAddConnectionResponse(AddConnectionResponsePacket packet) {
        if (packet.isConnectSuccess) {
            display();
        } else {
            JOptionPane.showMessageDialog(this, packet.message, "Server Message", JOptionPane.WARNING_MESSAGE);
            player.close();
            backToHome();
        }
    }

    private void display() {
        setVisible(true);
    }

    @Subscribe
    public void onUpdateRoomInfoEvent(UpdateRoomInfoPacket updateRoomInfoPacket) {
        renderPlayerList(updateRoomInfoPacket.clients);
        renderGameLevel(updateRoomInfoPacket.level);
    }

    @Subscribe
    public void onClosedServerEvent(ClosedServerNotificationPacket closedServerNotificationPacket) {
        backToHome();
        JOptionPane.showMessageDialog(this, closedServerNotificationPacket.message, "Room Closed", JOptionPane.WARNING_MESSAGE);
    }

    @Subscribe
    public void onStartGameEvent(StartGameResponsePacket startGameEvent) {
        startGame();
    }

    private void requestChangeGameLevel(String levelName) {
        ChangeGameLevelPacket packet = new ChangeGameLevelPacket(levels.indexOf(levelName));
        player.sendObject(packet);
    }

    public void renderPlayerList(ArrayList<ClientInRoom> clients) {
        System.out.println("-----------------------------RENDER-----------------------------");
        for (int i = 0; i < MAX_ROOM_SIZE; i++) {
            if (i < clients.size()) {
                ClientInRoom client = clients.get(i);
                if (client != null) {
                    PlayerHolder holder = playerHolders.get(i);
                    holder.getPlayerNameLb().setText(client.playerName);
                    if (client.playerName.equals(this.playerName)) {
                        holder.setFocusPlayer(true);
                    } else {
                        holder.setFocusPlayer(false);
                    }
                    holder.setReadyIcon(client.isReady);

                }
            } else {
                PlayerHolder holder = playerHolders.get(i);
                holder.getPlayerNameLb().setText("No Player");
                holder.setReadyIcon(false);
                holder.setFocusPlayer(false);
            }
        }
    }

    private void renderGameLevel(int level) {
        levelSelector.setSelectedIndex(level);
    }

    private void exitRoom() {
        room.shutdown();
    }

    private void backToHome() {
        if (screenManager == null)
            screenManager = ScreenManager.getInstance();
        screenManager.navigate(HOME_SCREEN);
        if (room != null)
            exitRoom();
        if (player != null)
            player.close();
        exitScreen();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn) {
            backToHome();
        } else if (e.getSource() == startGameBtn) {
            player.sendStartGameRequest(1);
            startGame();
        } else if (e.getSource() == readyBtn) {
            player.notifyReadyState(!player.isReady);
        }
    }

    private void startGame() {
        HashMap<String, Object> args = new HashMap<>();
        args.put("player", player);
        ScreenManager.getInstance().navigate(INGAME_SCREEN, args);
    }

    private void exitScreen() {
        EventBuz.getInstance().unregister(this);
    }
}
