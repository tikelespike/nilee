package com.tikelespike.nilee.data.entity.property;

import com.tikelespike.nilee.data.entity.GameEntity;

import javax.persistence.Entity;
import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

/**
 * A {@link ValueSelector} that always returns the highest value in the list.
 */
@Entity
public class MaxValueSelector<T extends Comparable<T>> extends GameEntity implements ValueSelector<T> {

    @Override
    public Optional<T> select(List<T> values) {
        return values.stream().max(Comparable::compareTo);
    }
}
