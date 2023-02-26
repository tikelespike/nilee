package com.tikelespike.nilee.data.entity.property;

import java.util.List;

/**
 *
 */
public interface ValueSelector<T> {
    T select(List<T> values);
}
