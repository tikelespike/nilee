package com.tikelespike.nilee.core.data.entity.property.events;

import com.tikelespike.nilee.core.data.entity.property.Property;
import com.tikelespike.nilee.core.events.Event;

public class ValueChangeEvent<T> extends Event {
    private final T oldValue;
    private final T newValue;

    public ValueChangeEvent(T oldValue, T newValue) {
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * @return the value {@link Property#getValue()} returned before the change
     */
    public T getOldValue() {
        return oldValue;
    }

    /**
     * @return the value {@link Property#getValue()} returns after the change
     */
    public T getNewValue() {
        return newValue;
    }
}
