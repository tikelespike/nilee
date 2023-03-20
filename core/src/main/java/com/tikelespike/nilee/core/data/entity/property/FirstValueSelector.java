package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.GameEntity;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

/**
 * A {@link ValueSelector} that always returns the first value in the list, if present.
 *
 * @param <T> the type of the values to select from
 */
@Entity
public class FirstValueSelector<T> extends ValueSelector<T> {

    @Override
    public Optional<T> select(List<T> values) {
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
    }

}
