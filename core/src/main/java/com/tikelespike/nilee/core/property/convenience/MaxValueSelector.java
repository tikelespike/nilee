package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.property.ValueSelector;

import java.util.List;
import java.util.Optional;

/**
 * A {@link ValueSelector} that always returns the highest value in the list.
 *
 * @param <T> the type of the values to select from
 */
public class MaxValueSelector<T extends Comparable<T>> extends ValueSelector<T> {

    @Override
    public Optional<T> select(List<T> values) {
        return values.stream().max(Comparable::compareTo);
    }
}
