package com.zyot.fung.shyn.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageLoader {
    public static BufferedImage image;
    public static BufferedImage entities;
    public static BufferedImage enemy, player;

    public static void init() {
        image = loadImage("/whte.png");
        entities = loadImage("/airplane.png");
        enemy = entities.getSubimage(0, 0,85, 90);
        player = entities.getSubimage(85, 0, 95, 90);
    }

    private static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
