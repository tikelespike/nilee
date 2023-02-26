package com.tikelespike.nilee.data.entity.property;

import com.tikelespike.nilee.data.entity.AbstractEntity;
import com.tikelespike.nilee.data.entity.GameEntity;

import javax.persistence.*;
import java.util.*;

@Entity
public class Property<T> extends AbstractEntity {

    @OneToMany(targetEntity = GameEntity.class, fetch = FetchType.EAGER) // fix for now, should this be lazy?
    private final Set<PropertyBaseSupplier<T>> baseValueSuppliers = new LinkedHashSet<>();

    @OneToMany(targetEntity = GameEntity.class, fetch = FetchType.EAGER) // fix for now, should this be lazy?
    private final Set<PropertyModifier<T>> modifiers = new LinkedHashSet<>();

    @OneToOne(targetEntity = GameEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL) // fix for now, should this be lazy?
    private ValueSelector<T> baseValueSelector = new FirstValueSelector<>();

    public Property() {

    }

    public Property(PropertyBaseSupplier<T> baseValueSupplier) {
        this.baseValueSuppliers.add(baseValueSupplier);
    }


    public T getValue() {
        T value = getBaseValue();
        for (PropertyModifier<T> modifier : getModifiers()) {
            value = modifier.apply(value);
        }
        return value;
    }

    public T getBaseValue() {
        return baseValueSelector.select(getBaseValueSuppliers().stream().map(PropertyBaseSupplier::getBaseValue).toList());
    }


    public Set<PropertyModifier<T>> getModifiers() {
        return new LinkedHashSet<>(modifiers);
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


    public Set<PropertyBaseSupplier<T>> getBaseValueSuppliers() {
        return new LinkedHashSet<>(baseValueSuppliers);
    }

    public void addBaseValueSupplier(PropertyBaseSupplier<T> baseValueSupplier) {
        baseValueSuppliers.add(baseValueSupplier);
    }

    public void removeBaseValueSupplier(PropertyBaseSupplier<T> baseValueSupplier) {
        baseValueSuppliers.remove(baseValueSupplier);
    }


    public void setBaseValueSelector(ValueSelector<T> baseValueSelector) {
        this.baseValueSelector = baseValueSelector;
    }

    public ValueSelector<T> getBaseValueSelector() {
        return baseValueSelector;
    }
}
