package com.sefford.test.core;

import com.sefford.kor.common.interfaces.Postable;

import de.greenrobot.event.EventBus;

/**
 * This class handles the event bus. We are using Greenrobot's.
 *
 *
 */
public class BusManager implements Postable {

    private final EventBus bus;

    public BusManager(EventBus bus) {
        this.bus = bus;
    }

    @Override
    public void post(Object o) {
        bus.post(o);
    }

    public void register(Object target) {
        this.bus.register(target);
    }

    public void unregister(Object target) {
        this.bus.unregister(target);
    }
}
