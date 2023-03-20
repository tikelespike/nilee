package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.GameEntity;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

/**
 * A {@link ValueSelector} that always returns the highest value in the list.
 */
@Entity
public class MaxValueSelector<T extends Comparable<T>> extends ValueSelector<T> {

    @Override
    public Optional<T> select(List<T> values) {
        return values.stream().max(Comparable::compareTo);
    }
}
