package com.zyot.fung.shyn.ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

import static com.zyot.fung.shyn.common.Constants.*;

public class PlayerHolder extends JPanel {
    private JLabel playerNameLb;

    public PlayerHolder(int x, int y) {
        setLayout(null);
        setSize(200, 300);
        setBorder(new LineBorder(Color.BLACK));
        setBounds(x, y, PLAYER_HOLDER_WIDTH, PLAYER_HOLDER_HEIGHT);
        init();
    }

    private void init() {
        playerNameLb = new JLabel("Player", SwingConstants.CENTER);
        playerNameLb.setBounds(20, 260, 160, 30);
        playerNameLb.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));

        add(playerNameLb);
    }
}
