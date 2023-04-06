package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.property.convenience.ManualOverrideModifier;

public class OverridableProperty<T> extends Property<T> {

    // invariant: overrideValue of overrideModifier is null iff. overrideModifier is not in this property's list of modifiers
    private final ManualOverrideModifier<T> overrideModifier = new ManualOverrideModifier<>(null);

    public OverridableProperty() {
        super();
    }

    public OverridableProperty(PropertyBaseSupplier<T> baseValueSupplier) {
        super(baseValueSupplier);
    }

    public void setOverride(T override) {
        if (override == null) {
            removeModifier(overrideModifier);
        } else {
            if (!isOverridden()) {
                addModifier(overrideModifier);
            }
        }
        overrideModifier.setOverrideValue(override);
    }

    public void removeOverride() {
        setOverride(null);
    }

    public T getOverride() {
        return overrideModifier.getOverrideValue();
    }

    public boolean isOverridden() {
        return getOverride() != null;
    }

    @Override
    public void addModifier(PropertyModifier<T> modifier) {
        // we want the override modifier to always stay the last in the list, since it's explicitly set by the user
        // and should therefore have the highest priority
        if (getModifiers().get(getModifiers().size() - 1).equals(overrideModifier)) {
            super.addModifier(getModifiers().size() - 1, modifier);
        }
    }
}
