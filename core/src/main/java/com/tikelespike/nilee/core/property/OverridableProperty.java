package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.property.convenience.ManualOverrideModifier;
import jakarta.validation.constraints.NotNull;

/**
 * A {@link Property} that can be overridden (usually by the user) with a fixed value. This is done by adding a
 * {@link ManualOverrideModifier} to the property. When other modifiers are added while the property is overridden, they
 * are added <i>before</i> the override modifier, so that the override modifier is always the last modifier in the list
 * (and therefore has the highest priority).
 *
 * @param <T> the type of the property
 */
public class OverridableProperty<T> extends Property<T> {

    // invariant: overrideValue of overrideModifier is null iff. overrideModifier is not in this property's list of
    // modifiers
    private final ManualOverrideModifier<T> overrideModifier = new ManualOverrideModifier<>(null);

    /**
     * Creates a new {@link OverridableProperty} with no base value supplier.
     * <p>
     * You must add a base value supplier before you can use this property.
     */
    public OverridableProperty() {
        super();
    }

    /**
     * Creates a property with the given default base value supplier.
     *
     * @param baseValueSupplier supplies the default value returned when calling {@link #getBaseValue()}.
     */
    public OverridableProperty(@NotNull PropertyBaseSupplier<T> baseValueSupplier) {
        super(baseValueSupplier);
    }

    /**
     * Overrides this property with the given value. If the given value is null, the override is removed and the value
     * is calculated as usual.
     *
     * @param override the new, user-defined/hardcoded value of this property
     */
    public void setOverride(T override) {
        if (override == null) {
            removeModifier(overrideModifier);
            overrideModifier.setOverrideValue(override);
        } else {
            if (isOverridden()) {
                overrideModifier.setOverrideValue(override);
            } else {
                overrideModifier.setOverrideValue(override);
                addModifier(overrideModifier);
            }
        }
    }

    /**
     * Removes the override of this property.
     */
    public void removeOverride() {
        setOverride(null);
    }

    /**
     * @return the value this property is overridden with, or null if it is not overridden
     */
    public T getOverride() {
        return overrideModifier.getOverrideValue();
    }

    /**
     * @return true if this property is currently overridden, false otherwise
     */
    public boolean isOverridden() {
        return getOverride() != null;
    }

    @Override
    public void addModifier(PropertyModifier<T> modifier) {
        // we want the override modifier to always stay the last in the list, since it's explicitly set by the user
        // and should therefore have the highest priority
        // (except when the override modifier is already not the last in the list, which indicates the intent to add
        // modifiers with higher priority than the override modifier

        if (!getModifiers().isEmpty() && getModifiers().get(getModifiers().size() - 1).equals(overrideModifier)) {
            super.addModifier(getModifiers().size() - 1, modifier);
        } else {
            super.addModifier(modifier);
        }
    }
}
