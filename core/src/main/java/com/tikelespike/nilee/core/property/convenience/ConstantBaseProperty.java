package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.property.Property;

public class ConstantBaseProperty extends Property<Integer> {

    private final ConstantBaseValue constantBaseValue;

    protected ConstantBaseProperty() {
        constantBaseValue = null;
    }

    public ConstantBaseProperty(int defaultBase, String description) {
        constantBaseValue = new ConstantBaseValue(defaultBase, description);
        addBaseValueSupplier(constantBaseValue);
    }

    public void setDefaultBaseValue(int baseValue) {
        constantBaseValue.setDefaultBaseValue(baseValue);
    }

    public int getDefaultBaseValue() {
        return constantBaseValue.getBaseValue();
    }
}
