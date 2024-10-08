package com.tikelespike.nilee.core.property.events;

import com.tikelespike.nilee.core.events.Event;

/**
 * Is fired when a {@link com.tikelespike.nilee.core.property.Property} changes its value, or something about the way a
 * value is calculated. Old and new value can be equal.
 *
 * @param <T> the type of the value that changed, corresponds to the type of the
 *         {@link com.tikelespike.nilee.core.property.Property}
 */
public class ValueChangeEvent<T> extends Event {
    private final T oldValue;
    private final T newValue;

    /**
     * Creates a new {@link ValueChangeEvent}. Old and new value may be equal.
     *
     * @param oldValue the value {@link com.tikelespike.nilee.core.property.Property#getValue()} returned before
     *         the change causing this event
     * @param newValue the value {@link com.tikelespike.nilee.core.property.Property#getValue()} returns after
     *         the change causing this event
     */
    public ValueChangeEvent(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * @return the value {@link com.tikelespike.nilee.core.property.Property#getValue()} returned before the change. May
     *         be equal to {@link #getNewValue()}
     */
    public T getOldValue() {
        return oldValue;
    }

    /**
     * @return the value {@link com.tikelespike.nilee.core.property.Property#getValue()} returns after the change. May
     *         be equal to {@link #getOldValue()}
     */
    public T getNewValue() {
        return newValue;
    }
}
