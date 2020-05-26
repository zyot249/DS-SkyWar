package com.zyot.fung.shyn.ui;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class ImageLoader {
    public static BufferedImage image;
    public static BufferedImage entities;
    public static BufferedImage enemy, player, myPlane;
    public static BufferedImage bullet;

    public static void init() {
       image = loadImage("/whte.png");
       entities = loadImage("/airplane.png");
       enemy = loadImage("/airplane.png").getSubimage(0, 0,85, 90);
       player = loadImage("/plane2.png").getSubimage(5, 10, 290, 240);
       myPlane = loadImage("/plane2.png").getSubimage(5, 10, 290, 240);
       bullet = loadImage("/bullet4.png").getSubimage(500, 0, 280, 1280);
    }

    public static BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
