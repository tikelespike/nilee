package com.tikelespike.nilee.core.data.entity.property;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class ConstantBaseProperty extends Property<Integer> {

    @OneToOne
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
