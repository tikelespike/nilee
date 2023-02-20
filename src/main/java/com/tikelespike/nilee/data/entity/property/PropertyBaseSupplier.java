package com.tikelespike.nilee.data.entity.property;

public interface PropertyBaseSupplier<T> {

    T getBaseValue();

    String getAbstractDescription();
    String getSourceName();
}
