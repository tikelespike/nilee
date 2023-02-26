package com.tikelespike.nilee.data.entity.property;

import com.tikelespike.nilee.data.entity.AbstractEntity;
import com.tikelespike.nilee.data.entity.GameEntity;

import javax.persistence.*;
import java.util.*;

/**
 * Describes a value constructed from a base value and modifiers. The base value is chosen from all base values
 * provided by a set of base value suppliers using a selection strategy (like the first value or the maximum). The
 * modifiers are applied in the order they are added.
 * <p>
 * For example, a property could represent a character's armor class. The base value could be the standard AC
 * calculated as {@code 10 + DEX}.
 * Another way to calculate the base value could be given by armor worn, which may calculate the base AC as something
 * like {@code 12 + DEX}. The modifiers represent a number of temporary bonuses and penalties, such as a +2 bonus from a
 * shield, or a -1 penalty from a spell. The final value is automatically calculated from the selected base value
 * (default is the first one added) and the application of all modifiers.
 *
 * @param <T> the type of the value (typically, an integer or a dice roll like 3d4)
 */
@Entity
public class Property<T> extends AbstractEntity {

    @OneToMany(targetEntity = GameEntity.class, fetch = FetchType.EAGER) // fix for now, should this be lazy?
    private final Set<PropertyBaseSupplier<T>> baseValueSuppliers = new LinkedHashSet<>();

    @OneToMany(targetEntity = GameEntity.class, fetch = FetchType.EAGER) // fix for now, should this be lazy?
    private final Set<PropertyModifier<T>> modifiers = new LinkedHashSet<>();

    // fix for now, should this be lazy?
    @OneToOne(targetEntity = GameEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private ValueSelector<T> baseValueSelector = new FirstValueSelector<>();

    /**
     * Default constructor for JPA. Will not add any base value suppliers or modifiers. You must add at least one
     * base value supplier before calling {@link #getValue()} or {@link #getBaseValue()} by using
     * {@link #addBaseValueSupplier(PropertyBaseSupplier)}.
     */
    protected Property() {

    }

    /**
     * Creates a property with the given default base value supplier.
     *
     * @param baseValueSupplier supplies the default value returned when calling {@link #getBaseValue()}.
     */
    public Property(PropertyBaseSupplier<T> baseValueSupplier) {
        this.baseValueSuppliers.add(baseValueSupplier);
    }


    /**
     * Calculates the value of this property by applying all modifiers to the base value.
     *
     * @return the effective value of this property
     */
    public T getValue() {
        T value = getBaseValue();
        for (PropertyModifier<T> modifier : getModifiers()) {
            value = modifier.apply(value);
        }
        return value;
    }

    /**
     * Calculates the base value of this property by selecting from the base value suppliers using the base value
     * selection strategy. Base value suppliers are added using {@link #addBaseValueSupplier(PropertyBaseSupplier)}.
     * The selection strategy can be set using {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @return the base value of this property, as selected by the base value selector
     */
    public T getBaseValue() {
        return baseValueSelector.select(getBaseValueSuppliers().stream().map(PropertyBaseSupplier::getBaseValue).toList());
    }


    /**
     * Retrieves the set of modifiers affecting to this property. A modifier is applied after the base value is
     * calculated, and the modifiers are applied in order. The returned set is a copy of the internal set, so any
     * changes to the returned set will not affect the property.
     *
     * @return a copy of the set of modifiers applied to this property
     */
    public Set<PropertyModifier<T>> getModifiers() {
        return new LinkedHashSet<>(modifiers);
    }

    /**
     * Adds a modifier to this property. The modifier will be applied after the base value is calculated, and will
     * affect the end result retrieved by {@link #getValue()}. All modifiers are applied in the order they are added.
     *
     * @param modifier the modifier to add to this property
     */
    public void addModifier(PropertyModifier<T> modifier) {
        modifiers.add(modifier);
    }

    /**
     * Removes a modifier from this property. The modifier will no longer affect the end result retrieved by
     * {@link #getValue()}. If the modifier was not previously added, this method has no effect.
     *
     * @param modifier the modifier to remove from this property
     */
    public void removeModifier(PropertyModifier<T> modifier) {
        modifiers.remove(modifier);
    }

    /**
     * Removes all modifiers from this property. The property will no longer be affected by any modifiers.
     */
    public void clearModifiers() {
        modifiers.clear();
    }


    /**
     * Retrieves the set of base value suppliers used by this property. The base value suppliers are used to
     * calculate the base value of this property, which is then modified by the modifiers. The returned set is a copy
     * of the internal set, so any changes to the returned set will not affect the property. The base value is selected
     * using the base value selector, which can be set using {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @return a copy of the set of base value suppliers used by this property
     */
    public Set<PropertyBaseSupplier<T>> getBaseValueSuppliers() {
        return new LinkedHashSet<>(baseValueSuppliers);
    }

    /**
     * Adds a base value supplier to this property. The added supplier will provide a base value for the selection
     * strategy to choose from. The selection strategy can be set using
     * {@link #setBaseValueSelector(ValueSelector)}.
     *
     * @param baseValueSupplier the base value supplier to add to this property
     */
    public void addBaseValueSupplier(PropertyBaseSupplier<T> baseValueSupplier) {
        baseValueSuppliers.add(baseValueSupplier);
    }

    /**
     * Removes a base value supplier from this property. The removed supplier will no longer provide a base value for
     * the selection strategy to choose from. If the supplier was not previously added, this method has no effect.
     *
     * @param baseValueSupplier the base value supplier to remove from this property
     */
    public void removeBaseValueSupplier(PropertyBaseSupplier<T> baseValueSupplier) {
        baseValueSuppliers.remove(baseValueSupplier);
    }


    /**
     * Sets the strategy used to select the base value from all base values provided by the base value suppliers.
     * Typical strategies include selecting the first value, the highest value, or the lowest value. The default is
     * {@link FirstValueSelector}.
     *
     * @param baseValueSelector the strategy selecting the base value from all base values provided by the base value
     *                          suppliers
     */
    public void setBaseValueSelector(ValueSelector<T> baseValueSelector) {
        this.baseValueSelector = baseValueSelector;
    }

    /**
     * Retrieves the strategy used to select the base value from all base values provided by the base value suppliers.
     *
     * @return the strategy used to select the base value from all base values provided by the base value suppliers
     */
    public ValueSelector<T> getBaseValueSelector() {
        return baseValueSelector;
    }
}