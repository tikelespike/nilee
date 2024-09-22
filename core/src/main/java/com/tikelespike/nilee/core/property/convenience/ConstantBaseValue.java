package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;

/**
 * A {@link PropertyBaseSupplier} that wraps a constant value that can be accessed with getters and setters.
 */
public class ConstantBaseValue extends PropertyBaseSupplier<Integer> {

    private Integer baseValue;
    private LocalizedString sourceName;

    /**
     * Creates a new {@code ConstantBaseValue} with the given base value and source name. The base value is the value
     * that will be returned by {@link #getBaseValue()}.
     *
     * @param baseValue the base value to wrap
     * @param sourceName a short name describing the semantics of where the base value comes from (e.g. "Base
     *         Strength")
     */
    public ConstantBaseValue(int baseValue, LocalizedString sourceName) {
        this.baseValue = baseValue;
        this.sourceName = sourceName;
    }

    @Override
    public Integer getBaseValue() {
        return baseValue;
    }

    /**
     * Sets the base value that will be returned by {@link #getBaseValue()}.
     *
     * @param baseValue the new base value
     */
    public void setBaseValue(int baseValue) {
        this.baseValue = baseValue;
        update();
    }

    @Override
    public LocalizedString getAbstractDescription() {
        return t -> baseValue.toString();
    }

    @Override
    public LocalizedString getSourceName() {
        return sourceName;
    }

    /**
     * Sets the source name that will be returned by {@link #getSourceName()}. It describes the semantics of where the
     * base value comes from and should be suitable for display in a user interface. It should usually not contain
     * additional characters like parentheses, brackets or colons.
     *
     * @param sourceName a short name describing the semantics of where the base value comes from (e.g. "Base
     *         Strength")
     */
    public void setSourceName(LocalizedString sourceName) {
        this.sourceName = sourceName;
        update();
    }
}
