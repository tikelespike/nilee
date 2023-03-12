package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.GameEntity;

import javax.persistence.Entity;

/**
 * A {@link PropertyBaseSupplier} that wraps a constant value that can be accessed with getters and setters.
 */
@Entity
public class ConstantBaseValue extends GameEntity implements PropertyBaseSupplier<Integer> {

    private int baseValue;
    private String sourceName;

    /**
     * Default constructor for JPA. Should not be used directly.
     */
    protected ConstantBaseValue() {
        this(0, "(default)");
    }

    /**
     * Creates a new {@code ConstantBaseValue} with the given base value and source name. The base value is the value
     * that will be returned by {@link #getBaseValue()}.
     *
     * @param baseValue the base value to wrap
     * @param sourceName a short name describing the semantics of where the base value comes from (e.g. "Base Strength")
     */
    public ConstantBaseValue(int baseValue, String sourceName) {
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
    }

    @Override
    public String getAbstractDescription() {
        return baseValue + "";
    }

    @Override
    public String getSourceName() {
        return sourceName;
    }

    /**
     * Sets the source name that will be returned by {@link #getSourceName()}. It describes the semantics of where the
     * base value comes from and should be suitable for display in a user interface. It should usually not contain
     * additional characters like parentheses, brackets or colons.
     *
     * @param sourceName a short name describing the semantics of where the base value comes from (e.g. "Base Strength")
     */
    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
