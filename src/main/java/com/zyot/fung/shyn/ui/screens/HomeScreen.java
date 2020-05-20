package com.zyot.fung.shyn.ui.screens;

import com.zyot.fung.shyn.server.Utils;
import com.zyot.fung.shyn.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static com.zyot.fung.shyn.common.Constants.NEW_ROOM_SCREEN;
import static com.zyot.fung.shyn.common.Constants.NORMAL_FONT;

public class HomeScreen extends JPanel implements ActionListener{

    private ScreenManager screenManager;

    private JButton createGameBtn;
    private JButton joinGameBtn;
    private JButton quitGameBtn;
    private JLabel titleLb;

    public HomeScreen(int width, int height) {
        setSize(width, height);
        setLayout(null);
        initUI();
        setVisible(true);
    }

    private void initUI() {
        createGameBtn = new JButton("Create Game");
        joinGameBtn = new JButton("Join Game");
        quitGameBtn = new JButton("Quit");

        titleLb = new JLabel("FuDuSkyWar", SwingConstants.CENTER);

        createGameBtn.setBounds(560, 320, 220, 50);
        createGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        joinGameBtn.setBounds(560, 396, 220, 50);
        joinGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));
        quitGameBtn.setBounds(560, 472, 220, 50);
        quitGameBtn.setFont(new Font(NORMAL_FONT, Font.PLAIN, 24));

        titleLb.setBounds(475, 160, 390, 70);
        titleLb.setFont(new Font("Serif", Font.BOLD, 46));

        quitGameBtn.addActionListener(this);
        createGameBtn.addActionListener(this);
        joinGameBtn.addActionListener(this);

        add(createGameBtn);
        add(joinGameBtn);
        add(quitGameBtn);
        add(titleLb);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == quitGameBtn) {
            System.exit(0);
        } else if (e.getSource() == createGameBtn) {
            joinRoom(true);
        } else if (e.getSource() == joinGameBtn) {
            joinRoom(false);
        }
    }
    private String enterPlayerName() {
        String name = JOptionPane.showInputDialog(this, "Enter player name:", "PlayerInGame Name", JOptionPane.QUESTION_MESSAGE);
        if (name == null) {
            JOptionPane.showMessageDialog(this, "Please enter a nickname before starting game!");
        } else if (name.length() < 4) {
            JOptionPane.showMessageDialog(this, "Your nickname is too short(must be longer than 4)!");
        } else if (name.length() > 16) {
            JOptionPane.showMessageDialog(this, "Your nickname is too long(must be shorter than 16!");
        } else {
            return name;
        }
        return null;
    }


    private void joinRoom(Boolean isRoomMaster) {
        int port = 0;
        if (!isRoomMaster) {
            try {
                port = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter room ID:", "Room ID", JOptionPane.QUESTION_MESSAGE));
                if (port >= 50000) {
                    JOptionPane.showMessageDialog(this, "Room ID is always smaller than 50000!");
                    return;
                }
                if (Utils.availablePort(port)) {
                    JOptionPane.showMessageDialog(this, "Cannot find room with this ID!");
                    return;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Room ID must be a number!");
                return;
            }
        }
        String playerName = enterPlayerName();
        if (playerName != null) {
            HashMap<String, Object> args = new HashMap<>();
            args.put("playerName", playerName);
            args.put("isRoomMaster", isRoomMaster);
            if (port != 0) {
                args.put("port", port);
            }
            if (screenManager == null)
                screenManager = ScreenManager.getInstance();
            screenManager.navigate(NEW_ROOM_SCREEN, args);
        }
    }
}
