package com.zyot.fung.shyn.ui;

import com.zyot.fung.shyn.client.Player;
import com.zyot.fung.shyn.packet.ChangePlaneTypeRequestPacket;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ItemEvent;

import static com.zyot.fung.shyn.common.Constants.*;

public class PlayerHolder extends JPanel {
    private JLabel playerNameLb;
    private JPanel readyIcon;
    private ImagePanel avatar;
    private String[] planeTypes = {"Type 1", "Type 2", "Type 3"};
    private JComboBox<String> planeSelector;

    private Player player;

    public PlayerHolder(int x, int y) {
        setLayout(null);
        setSize(200, 300);
        setBorder(new LineBorder(Color.BLACK));
        setBounds(x, y, PLAYER_HOLDER_WIDTH, PLAYER_HOLDER_HEIGHT);
        initUI();
        player = null;
    }

    private void initUI() {
        playerNameLb = new JLabel("No PlayerInGame", SwingConstants.CENTER);
        playerNameLb.setBounds(20, 260, 160, 30);
        playerNameLb.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));

        avatar = new ImagePanel();
        avatar.setBounds(20, 30, 160, 170);

        readyIcon = new JPanel();
        readyIcon.setBounds(170, 0, 30, 30);
        readyIcon.setBorder(new LineBorder(Color.BLACK, 3));
        readyIcon.setBackground(Color.GREEN);
        readyIcon.setOpaque(false);

        planeSelector = new JComboBox<>(planeTypes);
        planeSelector.setBounds(20, 215, 160, 30);
        planeSelector.addItemListener(e -> {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                String planeType = e.getItem().toString();
                requestChangePlaneType(planeType);
            }
        });

        add(planeSelector);
        add(playerNameLb);
        add(readyIcon);
        add(avatar);
    }

    private void requestChangePlaneType(String planeType) {
        if (player == null) return;

        int type = 0;
        if (planeType.equals(planeTypes[1])) {
            type = 1;
        } else if (planeType.equals(planeTypes[2])) {
            type = 2;
        }
        ChangePlaneTypeRequestPacket packet = new ChangePlaneTypeRequestPacket(player.getId(), type);
        player.sendObject(packet);
    }

    public void setFocusPlayer(boolean isFocused) {
        if (isFocused) {
            setBorder(new LineBorder(Color.BLUE, 3));
            planeSelector.setEnabled(true);
        } else {
            setBorder(new LineBorder(Color.BLACK));
            planeSelector.setEnabled(false);
        }

    }

    public void setReadyIcon(boolean isReady) {
        readyIcon.setOpaque(isReady);
        repaint();
    }

    public void setImage(int planeType) {
        avatar.setImage(planeType);
    }

    public void setPlayerName(String name) {
        playerNameLb.setText(name);
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
