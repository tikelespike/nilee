package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

public class DiceConstant extends DiceExpression {

    private final int value;

    public DiceConstant(int value) {
        this.value = value;
    }

    @Override
    int evaluate() {
        return value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    LocalizedString toLocalizedString() {
        return t -> String.valueOf(value);
    }
}