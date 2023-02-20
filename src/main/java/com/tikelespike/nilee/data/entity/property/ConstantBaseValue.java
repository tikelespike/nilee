package com.tikelespike.nilee.data.entity.property;

public class ConstantBaseValue<T> implements PropertyBaseSupplier<T> {

    private final T baseValue;
    private final String sourceName;

    public ConstantBaseValue(T baseValue, String sourceName) {
        this.baseValue = baseValue;
        this.sourceName = sourceName;
    }

    @Override
    public T getBaseValue() {
        return baseValue;
    }

    @Override
    public String getAbstractDescription() {
        return baseValue.toString();
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }
}
