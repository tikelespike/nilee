package com.tikelespike.nilee.core.data.entity.property.events;

import com.tikelespike.nilee.core.events.EventListener;

/**
 * An event listener that listens to {@link ValueChangeEvent ValueChangeEvents}.
 * @param <T> the type of the value that changes
 */
public interface ValueChangeListener<T> extends EventListener<ValueChangeEvent<T>> {
}
