package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.property.PropertyModifier;

/**
 * A {@link PropertyModifier} that adds a fixed value to the value of the property it is applied to.
 */
public class AdditiveModifier extends PropertyModifier<Integer> {

    private int bonus;
    private String source;

    /**
     * Creates a new {@link AdditiveModifier} with the given bonus and source.
     *
     * @param bonus how much to add to the value of the property. Can be negative.
     * @param source a short name describing the source of the bonus, e.g. "Racial bonus"
     */
    public AdditiveModifier(int bonus, String source) {
        this.bonus = bonus;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value + bonus;
    }

    @Override
    public String getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public String getConcreteDescription() {
        return "+ " + bonus;
    }

    @Override
    public String getSourceName() {
        return source;
    }

    /**
     * Sets the offset applied by this modifier.
     *
     * @param offset how much to add to the value of the property. Can be negative.
     */
    public void setBonus(int offset) {
        this.bonus = offset;
        update();
    }

    /**
     * Returns the offset applied by this modifier.
     *
     * @return how much this modifier adds to the value of the property. Can be negative.
     */
    public int getBonus() {
        return bonus;
    }

    /**
     * Sets the source description of this modifier. This is used to identify the source of the modifier in the UI.
     *
     * @param source a short description for the source of the bonus, e.g. "Racial bonus"
     */
    public void setSource(String source) {
        this.source = source;
        update();
    }

    /**
     * Gets the source description of this modifier. This is used to identify the source of the modifier in the UI.
     *
     * @return a short description for the source of the bonus, e.g. "Racial bonus"
     */
    public String getSource() {
        return source;
    }
}
