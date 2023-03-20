package com.tikelespike.nilee.core.events;

public class EventStoreListener<T extends Event> implements EventListener<T> {

    private T event = null;

    @Override
    public void onEvent(T event) {
        this.event = event;
    }

    public T getLatestEvent() {
        return event;
    }
}
