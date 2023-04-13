package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.Property;

/**
 * A {@link Property} that has a standard/default base value that can be set and retrieved. For example, a character's
 * strength score has a standard value set during character creation, which represents the "canonical" base value of the
 * property.
 */
public class ConstantBaseProperty extends Property<Integer> {

    private final ConstantBaseValue constantBaseValue;

    /**
     * Creates a new {@link ConstantBaseProperty} with the given default base value and description.
     *
     * @param defaultBase the standard base value of the property (for example, the strength score of a character set during character creation)
     * @param description a short description of the source of the default value, e.g. "Base score"
     */
    public ConstantBaseProperty(int defaultBase, LocalizedString description) {
        constantBaseValue = new ConstantBaseValue(defaultBase, description);
        addBaseValueSupplier(constantBaseValue);
    }

    /**
     * Sets the default base value of the property. For example, a character's strength score has a standard value set
     * during character creation, which represents the "canonical" base value of the property.
     *
     * @param baseValue the new default base value of the property
     */
    public void setDefaultBaseValue(int baseValue) {
        constantBaseValue.setBaseValue(baseValue);
    }

    /**
     * Gets the default base value of the property. For example, a character's strength score has a standard value set
     * during character creation, which represents the "canonical" base value of the property.
     *
     * @return baseValue the new default base value of the property
     */
    public int getDefaultBaseValue() {
        return constantBaseValue.getBaseValue();
    }
}
