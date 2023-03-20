package com.tikelespike.nilee.core.data.entity.property;

import javax.persistence.Entity;

@Entity
public class ConstantBaseProperty<T> extends Property<T> {

    private final ConstantBaseValue<T> constantBaseValue;

    protected ConstantBaseProperty() {
        constantBaseValue = null;
    }

    public ConstantBaseProperty(T defaultBase, String description) {
        constantBaseValue = new ConstantBaseValue<>(defaultBase, description);
        addBaseValueSupplier(constantBaseValue);
    }

    public void setDefaultBaseValue(T baseValue) {
        constantBaseValue.setBaseValue(baseValue);
        notifyListeners();
    }

    public T getDefaultBaseValue() {
        return constantBaseValue.getBaseValue();
    }
}
