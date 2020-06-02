package com.zyot.fung.shyn.ui;

import java.awt.image.BufferedImage;

public class SpaceImageLoader {
    private static BufferedImage space;
    private static final int FRAME_WIDTH = 485;
    private static final int FRAME_HEIGHT = 322;
    private static int currentY;

    public static void init() {
        space = ImageLoader.loadImage("/spaceplane.png").getSubimage(0, 0, 485, 1170);
        currentY = 1170 - FRAME_HEIGHT;
    }

    public static BufferedImage getPlaneFrame() {
        if (currentY <= 0) {
            currentY = 1170 - FRAME_HEIGHT;
        }
        return space.getSubimage(0, currentY--, FRAME_WIDTH, FRAME_HEIGHT);
    }
}
