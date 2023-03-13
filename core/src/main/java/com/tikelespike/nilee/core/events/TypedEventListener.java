package com.tikelespike.nilee.core.events;

/**
 * Wrapper class for an event listener and the event type it is subscribed to.
 *
 * @param eventType the type of event this listener is subscribed to
 * @param listener the wrapped listener that is subscribed to the event type
 * @param <T> the type of event the wrapped listener processes
 */
public record TypedEventListener<T extends Event>(Class<? extends T> eventType, EventListener<T> listener) {
}
