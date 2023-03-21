package com.tikelespike.nilee.core.data.entity.property.convenience;

import com.tikelespike.nilee.core.data.entity.property.Property;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class ConstantBaseProperty extends Property<Integer> {

    @OneToOne(cascade = CascadeType.ALL)
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
