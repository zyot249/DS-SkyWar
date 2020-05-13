package com.zyot.fung.shyn.packet;

import java.io.Serializable;

public class ChangeGameLevelPacket implements Serializable {
    private static final long serialVersionUID = 1L;

    public int level;

    public ChangeGameLevelPacket() {
    }

    public ChangeGameLevelPacket(int level) {
        this.level = level;
    }
}
