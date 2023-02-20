package com.tikelespike.nilee.data.entity.property;

public interface PropertyModifier<T> {

    T apply(T value);

    String getAbstractDescription();
    String getConcreteDescription();
    String getSourceName();
}
