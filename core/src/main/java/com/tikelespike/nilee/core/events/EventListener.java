package com.tikelespike.nilee.core.events;

/**
 * An observer of events of a specific type. Can be registered on an {@link EventBus} to receive events of that type
 * dispatched on the bus.
 *
 * @param <T> the type of event this listener can receive
 */
public interface EventListener<T extends Event> {

    /**
     * Called when an event of the type this listener is registered for is fired on the event bus.
     *
     * @param event the event that triggered this callback
     */
    void onEvent(T event);

}
