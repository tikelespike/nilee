package com.tikelespike.nilee.data.entity.property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Property<T> {

    private final List<PropertyBaseSupplier<T>> baseValueSuppliers = new ArrayList<>();

    private final List<PropertyModifier<T>> modifiers = new ArrayList<>();
    private Function<List<T>, T> baseValueSelector = (baseValues) -> baseValues.get(0);

    public Property(T baseValue) {
        setBaseValue(baseValue);
    }

    public Property(T baseValue, String description) {
        setBaseValue(baseValue, description);
    }

    public Property(PropertyBaseSupplier<T> baseValueSupplier) {
        this.baseValueSuppliers.add(baseValueSupplier);
    }

    public T getValue() {
        T baseValue = getBaseValue();
        for (PropertyModifier<T> modifier : modifiers) {
            baseValue = modifier.apply(baseValue);
        }
        return baseValue;
    }

    public T getBaseValue() {
        return baseValueSelector.apply(baseValueSuppliers.stream().map(PropertyBaseSupplier::getBaseValue).toList());
    }

    public void setBaseValue(T baseValue) {
        setBaseValue(baseValue, "(default)");
    }

    public void setBaseValue(T baseValue, String description) {
        this.baseValueSuppliers.clear();
        this.baseValueSuppliers.add(new ConstantBaseValue<T>(baseValue, description));
    }

    public void addModifier(PropertyModifier<T> modifier) {
        modifiers.add(modifier);
    }

    public void removeModifier(PropertyModifier<T> modifier) {
        modifiers.remove(modifier);
    }

    public void clearModifiers() {
        modifiers.clear();
    }

    public List<PropertyModifier<T>> getModifiers() {
        return List.copyOf(modifiers);
    }

    public void addBaseValueSource(PropertyBaseSupplier<T> baseValueSupplier) {
        baseValueSuppliers.add(baseValueSupplier);
    }

    public void removeBaseValueSource(PropertyBaseSupplier<T> baseValueSupplier) {
        baseValueSuppliers.remove(baseValueSupplier);
    }

    public List<PropertyBaseSupplier<T>> getBaseValueSuppliers() {
        return List.copyOf(baseValueSuppliers);
    }

    public void setBaseValueSelector(Function<List<T>, T> baseValueSelector) {
        this.baseValueSelector = baseValueSelector;
    }

    public Function<List<T>, T> getBaseValueSelector() {
        return baseValueSelector;
    }
}
