package com.zyot.fung.shyn.ui;

import java.awt.*;
import java.awt.image.BufferStrategy;

public class GameSetup implements  Runnable {
    private String title;
    private int width;
    private int height;
    private Thread thread;
    private boolean running;

    public static final int GAME_WIDTH = 400;
    public static final int GAME_HEIGHT = 400;

    private Display display;
    private GameManager manager;
    private BufferStrategy buffer;
    private Graphics g;

    public GameSetup(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void run() {
        LoadImage.init();
        display = new Display(title, width, height);
        manager = new GameManager();

        manager.init();

        int fps = 120;

        double timePerTick = 1000000000 / fps;
        double delta = 0;
        long current = System.nanoTime();

        while (running) {
            delta = delta + (System.nanoTime() - current) / timePerTick;
            current = System.nanoTime();
            if (delta >= 1) {
                tick();
                render();
                delta--;
            }
        }
    }

    public synchronized void start() {
        if (running) {
            return;
        }

        running = true;
        if (thread == null) {
            thread = new Thread(this);
            thread.start();
        }
    }

    public synchronized void stop() {
        if (running) {
            running = false;
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void tick() {
        manager.tick();
    }

    public void render() {
        buffer = display.getCanvas().getBufferStrategy();
        if (buffer == null) {
            display.getCanvas().createBufferStrategy(3);
            return;
        }
        g = buffer.getDrawGraphics();
        g.clearRect(0,0,width, height);

        // draw
        g.drawImage(LoadImage.image, 50 ,50, GAME_WIDTH, GAME_HEIGHT, null);
        manager.render(g);
        // end of draw

        buffer.show();
        g.dispose();
    }
}
