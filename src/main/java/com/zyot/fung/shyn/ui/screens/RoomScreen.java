package com.zyot.fung.shyn.ui.screens;

import com.google.common.eventbus.Subscribe;
import com.zyot.fung.shyn.client.EventBuz;
import com.zyot.fung.shyn.client.Player;
import com.zyot.fung.shyn.packet.AddConnectionRequestPacket;
import com.zyot.fung.shyn.packet.ClosedServerNotificationPacket;
import com.zyot.fung.shyn.packet.UpdateRoomInfoPacket;
import com.zyot.fung.shyn.server.ClientInRoom;
import com.zyot.fung.shyn.server.Room;
import com.zyot.fung.shyn.ui.PlayerHolder;
import com.zyot.fung.shyn.ui.ScreenManager;

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
    private ArrayList<PlayerHolder> playerHolders;
    private int[] playerHolderLocations = {20, 240, 460, 680};

    private Room room;

    private Player player;
    public String playerName;



    public RoomScreen(int width, int height, HashMap<String, Object> args) {
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(true);
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
    public void onUpdateRoomInfoEvent(UpdateRoomInfoPacket updateRoomInfoPacket) {
        renderPlayerList(updateRoomInfoPacket.clients);
    }

    @Subscribe
    public void onClosedServerEvent(ClosedServerNotificationPacket closedServerNotificationPacket) {
        JOptionPane.showMessageDialog(this, "Room is closed by master!", "Room Closed", JOptionPane.WARNING_MESSAGE);
        backToHome();
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
                        holder.setFocusPlayer();
                    }
                    holder.setReadyIcon(client.isReady);
                    System.out.println("In PlayerHolder: " + holder.getPlayerNameLb().getText());
                    System.out.println("Client " + client.playerName + " ready? " + client.isReady);

                }
            } else {
                PlayerHolder holder = playerHolders.get(i);
                holder.getPlayerNameLb().setText("No Player");
                holder.setReadyIcon(false);
            }
        }
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
        EventBuz.getInstance().unregister(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn) {
            backToHome();
        } else if (e.getSource() == startGameBtn) {

        } else if (e.getSource() == readyBtn) {
            player.notifyReadyState(!player.isReady);
        }
    }
}
