package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;

public class HPMaxBaseSupplier extends PropertyBaseSupplier<Integer> {

    private Property<Integer> hpMaxBaseProperty;

    protected HPMaxBaseSupplier() {
    }

    public HPMaxBaseSupplier(Property<Integer> hpMaxBaseProperty) {
        setHpMaxBaseProperty(hpMaxBaseProperty);
    }

    @Override
    public Integer getBaseValue() {
        return hpMaxBaseProperty.getValue();
    }

    @Override
    public String getAbstractDescription() {
        return "CON + rolled hit dice";
    }

    @Override
    public String getSourceName() {
        return "Base Hit Point Max";
    }


    // JPA setters and getters

    private Property<Integer> getHpMaxBaseProperty() {
        return hpMaxBaseProperty;
    }

    private void setHpMaxBaseProperty(Property<Integer> hpMaxBaseProperty) {
        this.hpMaxBaseProperty = hpMaxBaseProperty;
        this.hpMaxBaseProperty.addValueChangeListener(event -> update());
    }
}
