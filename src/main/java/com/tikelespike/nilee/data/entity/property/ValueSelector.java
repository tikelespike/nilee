package com.tikelespike.nilee.data.entity.property;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface ValueSelector<T> {

    /**
     * Chooses a value from the given list of values and returns it. The value in the returned optional must be one
     * of the values in the list. The returned optional must be empty if and only if the list is empty.
     *
     * @param values the list of values to select from. The returned value must be one of these values.
     * @return an optional containing one of the values from the list, or an empty optional if the list is empty
     */
    Optional<T> select(List<T> values);
}
