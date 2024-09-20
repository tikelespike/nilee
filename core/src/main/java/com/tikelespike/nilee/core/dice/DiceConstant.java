package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

/**
 * Dice expression that always evaluates to a constant value. Wraps an integer.
 */
public class DiceConstant extends DiceExpression {

    private final int value;

    /**
     * Creates a new constant dice expression.
     *
     * @param value the constant value
     */
    public DiceConstant(int value) {
        this.value = value;
    }

    @Override
    public int evaluate() {
        return value;
    }

    @Override
    public DiceExpression evaluatePartially() {
        return this;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public LocalizedString toLocalizedString() {
        return t -> value < 0 ? "- " + Math.abs(value) : String.valueOf(value);
    }
}
