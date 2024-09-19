package com.tikelespike.nilee.core.events;

/**
 * A simple event listener that stores the latest event it received for later inspection.
 *
 * @param <T> the type of event this listener listens to
 */
public class EventStoreListener<T extends Event> implements EventListener<T> {

    private T storedEvent = null;

    @Override
    public void onEvent(T event) {
        this.storedEvent = event;
    }

    /**
     * @return the latest event that was received by this listener, or {@code null} if no event has been received yet
     */
    public T getLatestEvent() {
        return storedEvent;
    }
}
