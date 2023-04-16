package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

public class DiceConstant extends DiceExpression {

    private final int value;

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
    public LocalizedString toLocalizedString(boolean abbreviateD20) {
        return t -> String.valueOf(value);
    }
}