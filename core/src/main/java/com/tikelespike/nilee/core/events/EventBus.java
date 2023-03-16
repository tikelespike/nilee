package com.tikelespike.nilee.core.events;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic subject of the observer pattern. It allows to register listeners for specific event types, and firing
 * events which will be delivered to all listeners that are registered for the event type.
 * <p>
 * If a listener is registered
 * multiple times, it will be called multiple times. Listeners will be called if the event type is an instance of the
 * registered event type as in the {@code instanceof} relation.
 *
 * @see EventListener
 */
public class EventBus {
    private final List<TypedEventListener<?>> typedEventListeners = new ArrayList<>();

    /**
     * Subscribes an observer to a specific event type.
     * <p>
     * The listener will be called whenever an event of the given {@code eventType},
     * or of a subclass of the given type, is fired. For example, a listener of type {@code EventListener<Event>}
     * can be registered to only receive events of type {@code MyEvent}, where {@code MyEvent extends Event}. It will
     * then only receive fired events that are an instance of {@code MyEvent}, but it will receive them as {@code Event}
     * objects.
     * <p>
     * If a listener is registered multiple times, it will be called
     * multiple times.
     *
     * @param eventType the superclass of all events that the listener should be called for
     * @param listener  the listener to be called when an event of the given type is fired
     * @param <T>       the type of the event the listener processes (has to be a superclass of {@code eventType})
     * @return a registration object that can be used to unregister the listener
     */
    public <T extends Event> Registration registerListener(Class<? extends T> eventType, EventListener<T> listener) {
        TypedEventListener<T> typedListener = new TypedEventListener<>(eventType, listener);
        typedEventListeners.add(typedListener);
        return new Registration(this, typedListener);
    }

    /**
     * Unregisters a listener from this event bus. Should only be called by the {@link Registration} object, which
     * should be used to unsubscribe listeners.
     *
     * @param listener the listener to be removed
     * @param <T>      the type of the event the listener processes
     * @return true if the listener was removed, false if it was not registered
     */
    protected <T extends Event> boolean unregister(TypedEventListener<T> listener) {
        return typedEventListeners.remove(listener);
    }

    protected <T extends Event> boolean unregisterAll(TypedEventListener<T> listener) {
        boolean removed = typedEventListeners.contains(listener);
        while (typedEventListeners.remove(listener)) {}
        return removed;
    }

    /**
     * Checks if a listener is still subscribed to this event bus. Should only be called by the {@link Registration}.
     * Use the registration object to check if a listener is still subscribed.
     *
     * @param listener the listener to check
     * @param <T>     the type of the event the listener processes
     * @return true if the listener is (still) subscribed, false if it is not (no longer) subscribed
     */
    protected <T extends Event> boolean isSubscribed(TypedEventListener<T> listener) {
        return typedEventListeners.contains(listener);
    }

    /**
     * Dispatches an event to all listeners that are registered for the event type. This will notify all listeners
     * that are currently registered for the event type, or for a superclass of the event type, on this event bus.
     * The order in which the listeners are called is undefined.
     *
     * @param event the event to be dispatched
     */
        @SuppressWarnings({"unchecked", "rawtypes"})
    public void fireEvent(Event event) {
        // We have to do manual type checking here
        for (TypedEventListener typedListener : typedEventListeners) {
            if (typedListener.eventType().isInstance(event)) {
                // we just checked that the listener listens to this event type,
                // this is safe because of the type checking of the register method, which requires the eventType()
                // to be a subclass of the type parameter of the listener
                typedListener.listener().onEvent(event);
            }
        }
    }
}
