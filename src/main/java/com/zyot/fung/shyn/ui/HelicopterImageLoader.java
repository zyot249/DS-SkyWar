package com.zyot.fung.shyn.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HelicopterImageLoader {
    public static ArrayList<BufferedImage> frames;

    private static int curFrame;

    public static void init() {
        frames = new ArrayList<>(6);
        frames.add(0, ImageLoader.loadImage("/helicopter/heli-f1.png").getSubimage(21, 21, 160, 160));
        frames.add(1, ImageLoader.loadImage("/helicopter/heli-f2.png").getSubimage(21, 21, 160, 160));
        frames.add(2, ImageLoader.loadImage("/helicopter/heli-f3.png").getSubimage(21, 21, 160, 160));
        frames.add(3, ImageLoader.loadImage("/helicopter/heli-f4.png").getSubimage(21, 21, 160, 160));
        frames.add(4, ImageLoader.loadImage("/helicopter/heli-f5.png").getSubimage(21, 21, 160, 160));
        frames.add(5, ImageLoader.loadImage("/helicopter/heli-f6.png").getSubimage(21, 21, 160, 160));
        curFrame = 0;
    }

    public static BufferedImage getPlaneFrame() {
        BufferedImage frame = frames.get(curFrame);
        if (curFrame == 5) {
            curFrame = 0;
        } else {
            curFrame++;
        }
        return frame;
    }
}
