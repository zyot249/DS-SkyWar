package com.zyot.fung.shyn.ui.imagehandler;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class PlaneLoader {
    private int planeType;
    public int curFrame;

    public PlaneLoader(int planeType) {
        this.planeType = planeType;
        curFrame = 0;
    }

    public BufferedImage getPlaneFrame() {
        ArrayList<BufferedImage> frames = ImageLoader.getFramesOfPlane(planeType);
        BufferedImage frame = frames.get(curFrame);
        if (curFrame == frames.size() - 1) {
            curFrame = 0;
        } else {
            curFrame++;
        }
        return frame;
    }
}
