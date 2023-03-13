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
        baseValueSuppliers.add(constantBaseValue);
    }

    public void setBaseValue(T baseValue) {
        T oldValue = getValue();
        constantBaseValue.setBaseValue(baseValue);
        notifyListeners(oldValue);
    }

    public T getBaseValue() {
        return constantBaseValue.getBaseValue();
    }
}
