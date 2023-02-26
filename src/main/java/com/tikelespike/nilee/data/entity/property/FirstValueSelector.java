package com.tikelespike.nilee.data.entity.property;

import com.tikelespike.nilee.data.entity.GameEntity;

import javax.persistence.Entity;
import java.util.List;

/**
 *
 */
@Entity
public class FirstValueSelector<T> extends GameEntity implements ValueSelector<T> {

    @Override
    public T select(List<T> values) {
        return values.get(0);
    }

}
