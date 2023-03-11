package com.tikelespike.nilee.events;

import java.util.ArrayList;
import java.util.List;

public class EventBus {
    private final List<TypedEventListener<?>> typedEventListeners = new ArrayList<>();

    public <T extends Event> Registration registerListener(Class<? extends T> eventType, EventListener<T> listener) {
        TypedEventListener<T> typedListener = new TypedEventListener<>(eventType, listener);
        typedEventListeners.add(typedListener);
        return new Registration(this, typedListener);
    }

    protected <T extends Event> boolean unregister(TypedEventListener<T> listener) {
        return typedEventListeners.remove(listener);
    }

    protected <T extends Event> boolean isSubscribed(TypedEventListener<T> listener) {
        return typedEventListeners.contains(listener);
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
