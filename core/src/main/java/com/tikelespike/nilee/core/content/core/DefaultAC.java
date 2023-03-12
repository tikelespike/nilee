package com.tikelespike.nilee.core.content.core;

import com.tikelespike.nilee.core.data.entity.property.Property;
import com.tikelespike.nilee.core.data.entity.property.PropertyBaseSupplier;

/**
 * The default armor class for a character, computed as 10 + DEX.
 */
public class DefaultAC implements PropertyBaseSupplier<Integer> {

    private final Property<Integer> dex;

    /**
     * Creates a new default armor class value supplier.
     *
     * @param dex the dexterity property to use for computing the armor class, should be the one of the character whose
     *            armor class this is
     */
    public DefaultAC(Property<Integer> dex) {
        this.dex = dex;
    }

    @Override
    public Integer getBaseValue() {
        return 10 + ((dex.getValue() - 10) / 2);
    }

    @Override
    public String getAbstractDescription() {
        return "10 + DEX";
    }

    @Override
    public String getSourceName() {
        return "Default AC";
    }
}
