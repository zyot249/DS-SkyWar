package com.zyot.fung.shyn.packet;

import java.io.Serializable;

public class ReadyRequestPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public int id;
    public boolean isReady;
    public int position;

    public ReadyRequestPacket(int id, boolean isReady) {
        this.id = id;
        this.isReady = isReady;
    }
}
