package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.property.events.UpdateSubject;

import java.util.List;
import java.util.Optional;

/**
 * A strategy for selecting a value from a list of values, like always returning the first value, selecting the
 * "highest" or "lowest" value, and so on.
 * <p>
 * Usually, the implementing class should always return the same value for a given list of input values, that is, the
 * returned value should only depend on the input.
 * <p>
 * If the result for a given list of input values does change, the implementing class should notify its observers by
 * using an {@link com.tikelespike.nilee.core.events.EventBus}, overriding the
 * {@link UpdateSubject#addUpdateListener(com.tikelespike.nilee.core.events.EventListener)} method to register listeners
 * to the bus, and firing an {@link com.tikelespike.nilee.core.property.events.UpdateEvent} on a change.
 *
 * @param <T> the type of the values to select from
 */
public abstract class ValueSelector<T> extends UpdateSubject {

    /**
     * Chooses a value from the given list of values and returns it. The value in the returned optional must be one of
     * the values in the list. The returned optional must be empty if and only if the list is empty.
     * <p>
     * Usually, the implementing class should always return the same value for a given list of input values, that is,
     * the returned value should only depend on the input.
     * <p>
     * If the result for a given list of input values does change, the implementing class should notify its observers by
     * using an {@link com.tikelespike.nilee.core.events.EventBus}, overriding the
     * {@link UpdateSubject#addUpdateListener(com.tikelespike.nilee.core.events.EventListener)} method to register
     * listeners to the bus, and firing an {@link com.tikelespike.nilee.core.property.events.UpdateEvent} on a change.
     *
     * @param values the list of values to select from. The returned value must be one of these values.
     *
     * @return an optional containing one of the values from the list, or an empty optional if the list is empty
     */
    public abstract Optional<T> select(List<T> values);
}
