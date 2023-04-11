package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.property.PropertyModifier;

/**
 * A {@link PropertyModifier} that multiplies the value by a given factor.
 */
public class MultiplicativeModifier extends PropertyModifier<Integer> {

    private int factor;
    private String source;

    /**
     * Creates a new {@link MultiplicativeModifier} with the given factor and source.
     *
     * @param factor how much to multiply the value of the property by. Can be negative.
     * @param source a short description of the source of the factor (e.g. "Feline agility" doubling movement speed)
     */
    public MultiplicativeModifier(int factor, String source) {
        this.factor = factor;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value * factor;
    }

    @Override
    public String getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public String getConcreteDescription() {
        return "* " + factor;
    }

    @Override
    public String getSourceName() {
        return source;
    }

    /**
     * @param factor how much to multiply the value of the property by. Can be negative.
     */
    public void setFactor(int factor) {
        this.factor = factor;
        update();
    }

    /**
     * @return the factor this modifier multiplies the property by
     */
    public int getFactor() {
        return factor;
    }

    /**
     * @param source a short description of the source of the modifier (e.g. "Feline agility" doubling movement speed)
     */
    public void setSource(String source) {
        this.source = source;
        update();
    }
}
