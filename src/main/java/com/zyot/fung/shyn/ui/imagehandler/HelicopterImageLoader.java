package com.zyot.fung.shyn.ui.imagehandler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class HelicopterImageLoader {
    public ArrayList<BufferedImage> frames;

    private int curFrame;

    public HelicopterImageLoader(ArrayList<BufferedImage> frames) {
        this.frames = frames;
        curFrame = 0;
    }

    public BufferedImage getPlaneFrame() {
        BufferedImage frame = frames.get(curFrame);
        if (curFrame == 5) {
            curFrame = 0;
        } else {
            curFrame++;
        }
        return frame;
    }
}
