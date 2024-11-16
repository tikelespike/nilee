package com.tikelespike.nilee.core.property.events;

import com.tikelespike.nilee.core.events.Event;

import java.util.Objects;

/**
 * Is fired when a {@link com.tikelespike.nilee.core.property.Property} changes its value. Old and new value are
 * guaranteed to be different. If you want to be notified when something about the way the property is calculated
 * changes, even if it currently has no impact on the resulting value, register for {@link UpdateEvent UpdateEvents}.
 *
 * @param <T> the type of the value that changed, corresponds to the type of the
 *         {@link com.tikelespike.nilee.core.property.Property}
 */
public class ValueChangeEvent<T> extends Event {
    private final T oldValue;
    private final T newValue;

    /**
     * Creates a new {@link ValueChangeEvent}.
     *
     * @param oldValue the value {@link com.tikelespike.nilee.core.property.Property#getValue()} returned before
     *         the change causing this event
     * @param newValue the value {@link com.tikelespike.nilee.core.property.Property#getValue()} returns after
     *         the change causing this event
     */
    public ValueChangeEvent(T oldValue, T newValue) {
        if (Objects.equals(oldValue, newValue)) {
            throw new IllegalArgumentException("New and old value have to be different.");
        }
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
