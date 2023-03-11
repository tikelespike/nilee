package com.tikelespike.nilee.events;

public class Registration {
    private final EventBus bus;
    private final TypedEventListener<?> listener;

    public Registration(EventBus bus, TypedEventListener<?> listener) {
        this.bus = bus;
        this.listener = listener;
    }

    public boolean unregister() {
        return bus.unregister(listener);
    }

    public boolean isActive() {
        return bus.isSubscribed(listener);
    }
}
