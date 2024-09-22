package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.PropertyModifier;

/**
 * A {@link PropertyModifier} that overrides the value of the property it is applied to with a fixed value, usually set
 * manually by the user.
 *
 * @param <T> the type of the property to override
 */
public class ManualOverrideModifier<T> extends PropertyModifier<T> {

    private T overrideValue;

    /**
     * Creates a new {@link ManualOverrideModifier} with the given override value.
     *
     * @param overrideValue the value to override the property with
     */
    public ManualOverrideModifier(T overrideValue) {
        this.overrideValue = overrideValue;
    }

    /**
     * Sets the override value of this modifier.
     *
     * @param overrideValue the value to override the property with
     */
    public void setOverrideValue(T overrideValue) {
        this.overrideValue = overrideValue;
        update();
    }

    /**
     * @return the override value of this modifier
     */
    public T getOverrideValue() {
        return overrideValue;
    }

    @Override
    public T apply(T value) {
        return overrideValue;
    }

    @Override
    public LocalizedString getAbstractDescription() {
        return t -> t.translate("core.character.modifier.override.operator",
                t.translate("core.character.modifier.override.value"));
    }

    @Override
    public LocalizedString getConcreteDescription() {
        return t -> t.translate("core.character.modifier.override.operator", overrideValue);
    }

    @Override
    public LocalizedString getSourceName() {
        return t -> t.translate("core.character.modifier.override.source");
    }
}
