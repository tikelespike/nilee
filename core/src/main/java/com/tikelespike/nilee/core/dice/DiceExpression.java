package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

public abstract class DiceExpression {

    public abstract int evaluate();

    public abstract DiceExpression evaluatePartially();

    public abstract LocalizedString toLocalizedString(boolean abbreviateD20);

    public final LocalizedString toLocalizedString() {
        return toLocalizedString(false);
    }
}
