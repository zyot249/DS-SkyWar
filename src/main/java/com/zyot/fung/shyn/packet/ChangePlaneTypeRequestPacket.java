package com.zyot.fung.shyn.packet;

import java.io.Serializable;

public class ChangePlaneTypeRequestPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public int planeType;

    public ChangePlaneTypeRequestPacket(int id, int planeType) {
        this.id = id;
        this.planeType = planeType;
    }
}
