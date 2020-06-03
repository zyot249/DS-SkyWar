package com.zyot.fung.shyn.ui;

import com.zyot.fung.shyn.ui.imagehandler.ImageLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image;

    public ImagePanel() {
        image = ImageLoader.loadImage("/noplayerimage.png");
    }

    public void setImage(int planeType) {
        if (planeType == -1) {
            image = ImageLoader.loadImage("/noplayerimage.png");
        } else if (planeType == 0) {
            image = ImageLoader.loadImage("/plane1/frame_apngframe1.png").getSubimage(40, 50, 217, 200);
        } else if (planeType == 1) {
            image = ImageLoader.loadImage("/plane2/plane2-1.png").getSubimage(5, 10, 290, 240);
        } else if (planeType == 2) {
            image = ImageLoader.loadImage("/helicopter/heli-f1.png").getSubimage(21, 21, 160, 160);
        } else {
            image = ImageLoader.loadImage("/plane1/frame_apngframe1.png").getSubimage(40, 50, 217, 200);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 160, 170,this); // see javadoc for more info on the parameters
    }
}
