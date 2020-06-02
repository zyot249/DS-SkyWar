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
            image = ImageLoader.loadImage("/plane1.png").getSubimage(95, 55, 410, 385);
        } else if (planeType == 1) {
            image = ImageLoader.loadImage("/plane2.png").getSubimage(5, 10, 290, 240);
        } else if (planeType == 2) {
            image = ImageLoader.loadImage("/helicopter/heli-f1.png");
        } else {
            image = ImageLoader.loadImage("/plane1.png").getSubimage(95, 55, 410, 385);
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, 160, 170,this); // see javadoc for more info on the parameters
    }
}
