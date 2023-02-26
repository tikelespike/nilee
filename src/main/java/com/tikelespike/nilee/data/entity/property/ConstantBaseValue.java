package com.tikelespike.nilee.data.entity.property;

import com.tikelespike.nilee.data.entity.GameEntity;

import javax.persistence.Entity;

@Entity
public class ConstantBaseValue extends GameEntity implements PropertyBaseSupplier<Integer> {

    private int baseValue;
    private String sourceName;

    public ConstantBaseValue() {
        this(0, "(default)");
    }

    public ConstantBaseValue(int baseValue, String sourceName) {
        this.baseValue = baseValue;
        this.sourceName = sourceName;
    }

    @Override
    public Integer getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
    }

    @Override
    public String getAbstractDescription() {
        return baseValue + "";
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
