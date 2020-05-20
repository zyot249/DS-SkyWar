package com.zyot.fung.shyn.ui.screens;

import com.zyot.fung.shyn.server.Utils;
import com.zyot.fung.shyn.ui.ScreenManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import static com.zyot.fung.shyn.common.Constants.*;

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
        String ip = "";
        if (!isRoomMaster) {
            ip = JOptionPane.showInputDialog(this, "Enter room ID:", "Room ID", JOptionPane.QUESTION_MESSAGE);
            if (!Utils.validateIP(ip)) {
                JOptionPane.showMessageDialog(this, "Wrong room ID! Please try again!");
                return;
            }

            if (Utils.availablePort(ip, HOST_PORT)) {
                JOptionPane.showMessageDialog(this, "Server not found!");
                return;
            }
        }
        String playerName = enterPlayerName();
        if (playerName != null) {
            HashMap<String, Object> args = new HashMap<>();
            args.put("playerName", playerName);
            args.put("isRoomMaster", isRoomMaster);

            if (!ip.equals("")) {
                args.put("ip", ip);
            }

            if (screenManager == null)
                screenManager = ScreenManager.getInstance();
            screenManager.navigate(NEW_ROOM_SCREEN, args);
        }
    }
}
