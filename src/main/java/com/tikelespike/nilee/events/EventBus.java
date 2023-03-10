package com.tikelespike.nilee.events;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private final List<TypedEventListener<?>> typedEventListeners = new ArrayList<>();

    public <T extends Event> void registerListener(Class<? extends T> eventType, EventListener<T> listener) {
        typedEventListeners.add(new TypedEventListener<>(eventType, listener));
    }

    protected <T extends Event> void unregister(TypedEventListener<T> listener) {
        typedEventListeners.remove(listener);
    }

    public void fireEvent(Event event) {
        // We have to do manual type checking here
        for (TypedEventListener typedListener : typedEventListeners) {
            if (typedListener.eventType().isInstance(event)) {
                // we just checked that the listener listens to this event type,
                // so we can safely call this unchecked
                typedListener.listener().onEvent(event);
            }
        }
    }
}
