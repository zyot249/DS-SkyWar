package com.zyot.fung.shyn.client;

import com.google.common.eventbus.EventBus;

public class EventBuz {
    private static EventBus instance;

    public static EventBus getInstance() {
        if (instance == null) {
            synchronized (EventBuz.class) {
                if (null == instance) {
                    instance = new EventBus();
                }
            }
        }
        return instance;
    }
}
