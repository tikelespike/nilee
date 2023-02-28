package com.tikelespike.nilee.data.entity.property;

import com.tikelespike.nilee.data.entity.GameEntity;

import javax.persistence.Entity;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Entity
public class FirstValueSelector<T> extends GameEntity implements ValueSelector<T> {

    @Override
    public Optional<T> select(List<T> values) {
        return values.isEmpty() ? Optional.empty() : Optional.of(values.get(0));
    }

}
