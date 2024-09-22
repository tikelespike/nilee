package com.tikelespike.nilee.core.property.convenience;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.PropertyModifier;

/**
 * A {@link PropertyModifier} that multiplies the value by a given factor.
 */
public class MultiplicativeModifier extends PropertyModifier<Integer> {

    private int factor;
    private LocalizedString source;

    /**
     * Creates a new {@link MultiplicativeModifier} with the given factor and source.
     *
     * @param factor how much to multiply the value of the property by. Can be negative.
     * @param source a short description of the source of the factor (e.g. "Feline agility" doubling movement
     *         speed)
     */
    public MultiplicativeModifier(int factor, LocalizedString source) {
        this.factor = factor;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value * factor;
    }

    @Override
    public LocalizedString getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public LocalizedString getConcreteDescription() {
        return t -> t.translate("core.character.modifier.multiplicative.operator", factor);
    }

    @Override
    public LocalizedString getSourceName() {
        return source;
    }

    /**
     * @return the factor this modifier multiplies the property by
     */
    public int getFactor() {
        return factor;
    }

    /**
     * @param factor how much to multiply the value of the property by. Can be negative.
     */
    public void setFactor(int factor) {
        this.factor = factor;
        update();
    }

    /**
     * @param source a short description of the source of the modifier (e.g. "Feline agility" doubling movement
     *         speed)
     */
    public void setSource(LocalizedString source) {
        this.source = source;
        update();
    }
}
