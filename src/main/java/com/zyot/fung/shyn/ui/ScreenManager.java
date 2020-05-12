package com.zyot.fung.shyn.ui;

import com.zyot.fung.shyn.ui.screens.HomeScreen;
import com.zyot.fung.shyn.ui.screens.RoomScreen;

import javax.swing.*;

import static com.zyot.fung.shyn.common.Constants.*;

public class ScreenManager {
    private static ScreenManager instance;
    private HomeScreen homeScreen;
    private RoomScreen roomScreen;
    private JFrame window;

    private ScreenManager() {
        window = new JFrame("FuDuSkyWar");
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setResizable(false);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setVisible(false);

        navigate(HOME_SCREEN);
    }

    public void display() {
        window.setVisible(true);
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            synchronized (ScreenManager.class) {
                if (null == instance) {
                    instance = new ScreenManager();
                }
            }
        }
        return instance;
    }

    public void navigate(String screenName) {
        if (screenName.equals(HOME_SCREEN)) {
            window.getContentPane().removeAll();
            window.getContentPane().add(getHomeScreen());
            window.revalidate();
            window.repaint();
        } else if (screenName.equals(NEW_ROOM_SCREEN)) {
            window.getContentPane().removeAll();
            window.getContentPane().add(getNewRoomScreen());
            window.revalidate();
            window.repaint();
        } else if (screenName.equals(EXISTED_ROOM_SCREEN)) {
            window.getContentPane().removeAll();
            window.getContentPane().add(getRoomScreen());
            window.revalidate();
            window.repaint();
        }
    }

    private synchronized HomeScreen getHomeScreen() {
        if (homeScreen == null) {
            homeScreen = new HomeScreen(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        return homeScreen;
    }

    private synchronized RoomScreen getRoomScreen() {
        if (roomScreen == null) {
            roomScreen = new RoomScreen(SCREEN_WIDTH, SCREEN_HEIGHT);
        }
        return roomScreen;
    }

    private synchronized RoomScreen getNewRoomScreen() {
        roomScreen = new RoomScreen(SCREEN_WIDTH, SCREEN_HEIGHT);
        return roomScreen;
    }
}
