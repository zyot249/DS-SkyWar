package com.zyot.fung.shyn.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadImage {
    public static BufferedImage image;
    public static BufferedImage entities;
    public static BufferedImage enemy, player;

    public static void init() {
        image = imageLoader("/whte.png");
        entities = imageLoader("/airplane.png");
        enemy = entities.getSubimage(0, 0,85, 90);
        player = entities.getSubimage(85, 0, 95, 90);
    }

    private static BufferedImage imageLoader(String path) {
        try {
            return ImageIO.read(LoadImage.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
