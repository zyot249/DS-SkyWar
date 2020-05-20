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
       image = imageLoader("/whte.png");
       entities = imageLoader("/airplane.png");
       enemy = imageLoader("/airplane.png").getSubimage(0, 0,85, 90);
       player = imageLoader("/plane2.png").getSubimage(5, 10, 290, 240);
       myPlane = imageLoader("/plane2.png").getSubimage(5, 10, 290, 240);
       bullet = imageLoader("/bullet4.png").getSubimage(500, 0, 280, 1280);
    }

    private static BufferedImage imageLoader(String path) {
        try {
            System.out.println(path);
            System.out.println( ImageLoader.class.getResource(ImageLoader.class.getSimpleName() + ".class") );
            return ImageIO.read(ImageLoader.class.getResource(path));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return null;
    }
}
