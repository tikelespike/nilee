package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.property.PropertyModifier;

public class ManualOverrideModifier<T> extends PropertyModifier<T> {

    private T overrideValue;

    public ManualOverrideModifier(T overrideValue) {
        this.overrideValue = overrideValue;
    }

    public void setOverrideValue(T overrideValue) {
        this.overrideValue = overrideValue;
        update();
    }

    public T getOverrideValue() {
        return overrideValue;
    }

    @Override
    public T apply(T value) {
        return overrideValue;
    }

    @Override
    public String getAbstractDescription() {
        return "-> override value";
    }

    @Override
    public String getConcreteDescription() {
        return "-> " + overrideValue;
    }

    @Override
    public String getSourceName() {
        return "Custom Override";
    }
}
