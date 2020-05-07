package com.zyot.fung.shyn.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import static com.zyot.fung.shyn.common.Constants.HOME_SCREEN;
import static com.zyot.fung.shyn.common.Constants.NORMAL_FONT;

public class RoomScreen extends JPanel implements ActionListener {
    private ScreenManager screenManager;
    private JButton exitBtn;
    private JButton startGameBtn;
    private JSeparator separator;
    private ArrayList<PlayerHolder> playerHolders;
    private int[] playerHolderLocations = {20, 240, 460, 680};

    public RoomScreen(int width, int height) {
        setSize(width, height);
        setLayout(null);
        init();
        setVisible(true);
    }

    private void init() {
        exitBtn = new JButton("Exit Room");
        startGameBtn = new JButton("Start Game");
        separator = new JSeparator();
        playerHolders = new ArrayList<>();
        for (int i = 0; i < playerHolderLocations.length; i++) {
            playerHolders.add(new PlayerHolder(playerHolderLocations[i], 210));
            add(playerHolders.get(i));
        }

        exitBtn.setBounds(20, 10, 110, 25);
        exitBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 14));
        startGameBtn.setBounds(660, 540, 220, 50);
        startGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 26));
        separator.setBounds(20, 525, 860, 10);

        exitBtn.addActionListener(this);

        add(exitBtn);
        add(startGameBtn);
        add(separator);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == exitBtn) {
            if (screenManager == null)
                screenManager = ScreenManager.getInstance();
            screenManager.navigate(HOME_SCREEN);
        }
    }
}
