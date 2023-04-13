package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

public abstract class DiceExpression {

    abstract int evaluate();

    abstract LocalizedString toLocalizedString();

}
