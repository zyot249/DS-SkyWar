package com.zyot.fung.shyn.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlaneImageLoader {
    public static ArrayList<BufferedImage> frames;

    public static int curFrame;

    public static void init() {
        frames = new ArrayList<>(3);
        frames.add(0, ImageLoader.loadImage("/plane2/plane2-1.png").getSubimage(5, 10, 290, 240));
        frames.add(1, ImageLoader.loadImage("/plane2/plane2-2.png").getSubimage(5, 10, 290, 240));
        frames.add(2, ImageLoader.loadImage("/plane2/plane2-3.png").getSubimage(5, 10, 290, 240));
        curFrame = 0;
    }

    public static BufferedImage getPlaneFrame() {
        BufferedImage frame = frames.get(curFrame);
        if (curFrame == 2) {
            curFrame = 0;
        } else {
            curFrame++;
        }
        return frame;
    }
}
