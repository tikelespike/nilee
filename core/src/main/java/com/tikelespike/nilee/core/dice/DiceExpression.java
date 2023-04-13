package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

public abstract class DiceExpression {

    public abstract int evaluate();

    public abstract LocalizedString toLocalizedString();

}
