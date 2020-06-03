package com.zyot.fung.shyn.ui.imagehandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class ImageLoader {
    public static BufferedImage entities;
    public static BufferedImage enemy, player, myPlane;
    public static BufferedImage playerBullet, enemyBullet;
    public static ArrayList<BufferedImage> framesOfPlane1;
    public static ArrayList<BufferedImage> framesOfPlane2;
    public static ArrayList<BufferedImage> framesOfPlane3;

    public static void init() {
        SpaceImageLoader.init();
//        entities = loadImage("/airplane.png");
        enemy = loadImage("/airplane.png").getSubimage(0, 0,85, 90);
//        player = loadImage("/plane2.png").getSubimage(5, 10, 290, 240);
//        myPlane = loadImage("/plane2.png").getSubimage(5, 10, 290, 240);
        playerBullet = loadImage("/bullet4.png").getSubimage(500, 0, 280, 1280);
        enemyBullet = loadImage("/bullet5-reverse.png").getSubimage(165, 40, 107, 295);

        framesOfPlane3 = new ArrayList<>(6);
        framesOfPlane3.add(0, ImageLoader.loadImage("/helicopter/heli-f1.png").getSubimage(21, 21, 160, 160));
        framesOfPlane3.add(1, ImageLoader.loadImage("/helicopter/heli-f2.png").getSubimage(21, 21, 160, 160));
        framesOfPlane3.add(2, ImageLoader.loadImage("/helicopter/heli-f3.png").getSubimage(21, 21, 160, 160));
        framesOfPlane3.add(3, ImageLoader.loadImage("/helicopter/heli-f4.png").getSubimage(21, 21, 160, 160));
        framesOfPlane3.add(4, ImageLoader.loadImage("/helicopter/heli-f5.png").getSubimage(21, 21, 160, 160));
        framesOfPlane3.add(5, ImageLoader.loadImage("/helicopter/heli-f6.png").getSubimage(21, 21, 160, 160));

        framesOfPlane2 = new ArrayList<>(3);
        framesOfPlane2.add(0, ImageLoader.loadImage("/plane2/plane2-1.png").getSubimage(5, 10, 290, 240));
        framesOfPlane2.add(1, ImageLoader.loadImage("/plane2/plane2-2.png").getSubimage(5, 10, 290, 240));
        framesOfPlane2.add(2, ImageLoader.loadImage("/plane2/plane2-3.png").getSubimage(5, 10, 290, 240));

        framesOfPlane1 = new ArrayList<>(6);
        framesOfPlane1.add(0, ImageLoader.loadImage("/plane1/frame_apngframe1.png").getSubimage(40, 50, 217, 200));
        framesOfPlane1.add(1, ImageLoader.loadImage("/plane1/frame_apngframe2.png").getSubimage(40, 50, 217, 200));
        framesOfPlane1.add(2, ImageLoader.loadImage("/plane1/frame_apngframe3.png").getSubimage(40, 50, 217, 200));
        framesOfPlane1.add(3, ImageLoader.loadImage("/plane1/frame_apngframe4.png").getSubimage(40, 50, 217, 200));
        framesOfPlane1.add(4, ImageLoader.loadImage("/plane1/frame_apngframe5.png").getSubimage(40, 50, 217, 200));
        framesOfPlane1.add(5, ImageLoader.loadImage("/plane1/frame_apngframe6.png").getSubimage(40, 50, 217, 200));

    }


    public static ArrayList<BufferedImage> getFramesOfPlane(int planeType) {
        switch (planeType) {
            case 1: return framesOfPlane2;
            case 2: return framesOfPlane3;
            default: return framesOfPlane1;
        }
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
