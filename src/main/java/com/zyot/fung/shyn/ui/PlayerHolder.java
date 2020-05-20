package com.zyot.fung.shyn.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static com.zyot.fung.shyn.common.Constants.*;

public class PlayerHolder extends JPanel {
    private JLabel playerNameLb;
    private JPanel readyIcon;
    private ImagePanel avartar;

    public PlayerHolder(int x, int y) {
        setLayout(null);
        setSize(200, 300);
        setBorder(new LineBorder(Color.BLACK));
        setBounds(x, y, PLAYER_HOLDER_WIDTH, PLAYER_HOLDER_HEIGHT);
        initUI();
    }

    private void initUI() {
        playerNameLb = new JLabel("No PlayerInGame", SwingConstants.CENTER);
        playerNameLb.setBounds(20, 260, 160, 30);
        playerNameLb.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));

        avartar = new ImagePanel();
        avartar.setBounds(20, 30, 160, 170);

        readyIcon = new JPanel();
        readyIcon.setBounds(170, 0, 30, 30);
        readyIcon.setBorder(new LineBorder(Color.BLACK, 3));
        readyIcon.setBackground(Color.GREEN);
        readyIcon.setOpaque(false);

        add(playerNameLb);
        add(readyIcon);
        add(avartar);
    }

    public void setFocusPlayer(boolean isFocused) {
        if (isFocused) {
            setBorder(new LineBorder(Color.BLUE, 3));
        } else {
            setBorder(new LineBorder(Color.BLACK));
        }

    }

    public void setReadyIcon(boolean isReady) {
        readyIcon.setOpaque(isReady);
        repaint();
    }

//    public JLabel getPlayerNameLb() {
//        return playerNameLb;
//    }

    public void setImage(boolean isExistPlayer) {
        avartar.setImage(isExistPlayer);
    }

    public void setPlayerName(String name) {
        playerNameLb.setText(name);
    }
}
