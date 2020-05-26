package com.zyot.fung.shyn.ui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class SpaceImageLoader {
    private static final int NUM_OF_SAME_FRAME = 4;
    public static ArrayList<BufferedImage> frames;

    private static int curFrame;
    private static int count;

    public static void init() {
        frames = new ArrayList<>(102);
        for (int i = 1; i <= 102; i++) {
            if (i < 10) {
                frames.add(i - 1, ImageLoader.loadImage("/space1/ezgif-frame-00" + i + ".jpg"));
            } else if (i < 100) {
                frames.add(i - 1, ImageLoader.loadImage("/space1/ezgif-frame-0" + i + ".jpg"));
            } else {
                frames.add(i - 1, ImageLoader.loadImage("/space1/ezgif-frame-" + i + ".jpg"));
            }
        }
        curFrame = 0;
        count = 1;
    }

    public static BufferedImage getPlaneFrame() {
        BufferedImage frame = frames.get(curFrame);
        if (count < NUM_OF_SAME_FRAME) {
            count++;
        } else if (count == NUM_OF_SAME_FRAME) {
            if (curFrame == 101) {
                curFrame = 0;
            } else {
                curFrame++;
            }
            count = 1;
        }
        return frame;
    }
}
