package com.zyot.fung.shyn.ui.screens;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.client.Player;
import com.zyot.fung.shyn.common.PlayerInGame;
import com.zyot.fung.shyn.packet.*;
import com.zyot.fung.shyn.server.ClientInRoom;
import com.zyot.fung.shyn.server.Room;
import com.zyot.fung.shyn.server.Utils;
import com.zyot.fung.shyn.ui.PlayerHolder;
import com.zyot.fung.shyn.ui.ScreenManager;
import com.zyot.fung.shyn.ui.imagehandler.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import static com.zyot.fung.shyn.common.Constants.*;

public class RoomScreen extends JPanel implements ActionListener {
    private ScreenManager screenManager;
    private JButton exitBtn;
    private JButton startGameBtn;
    private JButton readyBtn;
    private JSeparator separator;
    private JLabel roomIDLb;
    private ArrayList<PlayerHolder> playerHolders;
    private int[] playerHolderLocations = {20, 240, 460, 680};
    private Room room;
    private String host;

    private Player player;
    public String playerName;

    public RoomScreen(int width, int height, HashMap<String, Object> args) {
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(false);
        if (args != null) {
            boolean isMaster = false;
            if (args.containsKey("isRoomMaster")) {
                if ((Boolean)args.get("isRoomMaster")) {
                    initRoomServer();
                    isMaster = (boolean) args.get("isRoomMaster");
                    this.host = Utils.getLocalAddress();

                    renderUIofMaster();
                } else {
                    if (args.containsKey("ip")) {
                        this.host = (String) args.get("ip");
                    }
                }
            }

            if (args.containsKey("playerName")) {
                this.playerName = args.get("playerName").toString();
                initPlayer(isMaster, this.host);
            }
        }
    }

    private void renderUIofMaster() {
        startGameBtn = new JButton("Start Game");
        startGameBtn.setBounds(660, 540, 220, 50);
        startGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        startGameBtn.addActionListener(this);

        roomIDLb = new JLabel("Room ID: " + this.host);
        roomIDLb.setForeground(Color.WHITE);
        roomIDLb.setBounds(600, 10, 300, 25);

        add(roomIDLb);
        add(startGameBtn);
        remove(readyBtn);
    }

    private void initRoomServer() {
        room = new Room();
        room.start();
    }

    private void initPlayer(boolean isMaster, String host) {
        player = new Player(host, HOST_PORT);
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

        exitBtn.setBounds(20, 10, 110, 25);
        exitBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));
        readyBtn.setBounds(660, 540, 220, 50);
        readyBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        separator.setBounds(20, 525, 860, 10);

        exitBtn.addActionListener(this);
        readyBtn.addActionListener(this);

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
    }

    @Subscribe
    public void onClosedServerEvent(ClosedServerNotificationPacket closedServerNotificationPacket) {
        backToHome();
        JOptionPane.showMessageDialog(this, closedServerNotificationPacket.message, "Room Closed", JOptionPane.WARNING_MESSAGE);
    }

    @Subscribe
    public void onStartGameEvent(StartGameResponsePacket startGameEvent) {
        startGame(startGameEvent.playerInGames);
    }

    @Subscribe
    public void onNotReadyWarningEvent(NotReadyWarningPacket notReadyWarningPacket) {
        JOptionPane.showMessageDialog(this, notReadyWarningPacket.message, "Warning", JOptionPane.WARNING_MESSAGE);
    }

    @Subscribe
    public void onServerNotFoundEvent(ServerNotFoundPacket serverNotFoundPacket) {
        JOptionPane.showMessageDialog(this, serverNotFoundPacket.message, "Warning", JOptionPane.WARNING_MESSAGE);
        backToHome();
    }

    public void renderPlayerList(ArrayList<ClientInRoom> clients) {
        System.out.println("-----------------------------RENDER-----------------------------");
        for (int i = 0; i < MAX_ROOM_SIZE; i++) {
            if (i < clients.size()) {
                ClientInRoom client = clients.get(i);
                if (client != null) {
                    PlayerHolder holder = playerHolders.get(i);
                    holder.setPlayerName(client.playerName);
                    if (client.playerName.equals(this.playerName)) {
                        holder.setFocusPlayer(true);
                        holder.setPlayer(player);
                    } else {
                        holder.setPlaneType(client.planeType);
                        holder.setFocusPlayer(false);
                    }
                    holder.setReadyIcon(client.isReady);
                    holder.setImage(client.planeType);
                }
            } else {
                PlayerHolder holder = playerHolders.get(i);
                holder.setPlayer(null);
                holder.setPlayerName("No Player");
                holder.setImage(-1);
                holder.setReadyIcon(false);
                holder.setFocusPlayer(false);
                holder.setPlaneType(0);
            }
        }
    }

    private void exitRoom() {
        room.doBeforeClose();
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
            player.sendStartGameRequest();
        } else if (e.getSource() == readyBtn) {
            player.notifyReadyState(!player.isReady);
        }
    }

    private void startGame(ArrayList<PlayerInGame> playerInGames) {
        HashMap<String, Object> args = new HashMap<>();
        args.put("playerInGames", playerInGames);
        args.put("player", player);
        ScreenManager.getInstance().navigate(INGAME_SCREEN, args);
    }

    private void exitScreen() {
        EventBuz.getInstance().unregister(this);
    }

    public void resetReadyStatus() {
        if (player.getId() != 0) {  // room master always has id = 0
            player.notifyReadyState(false);
        } else {
            player.notifyReadyState(true);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(ImageLoader.loadImage("/background4.jpg"), 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
    }
}
