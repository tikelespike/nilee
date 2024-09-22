package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

/**
 * A dice expression that can be evaluated to a number by rolling dice. For example, "2d6 + 1d4 + 3" usually means a
 * dice expression that can be evaluated to a number by rolling two six-sided dice, one four-sided die, adding the
 * results, then adding three.
 */
public abstract class DiceExpression {

    /**
     * Evaluates this dice expression by rolling all dice and evaluating the operands.
     *
     * @return the result of evaluating this dice expression by rolling dice.
     */
    public abstract int evaluate();

    /**
     * Only rolls all dice in this expression, but does not evaluate the operands.
     *
     * @return a dice expression that can be evaluated to a number by evaluating the operands.
     */
    public abstract DiceExpression evaluatePartially();

    /**
     * Converts this dice expression to a localized string representation, like "2d6 + 1d4 + 3".
     *
     * @return a localized string representation of this dice expression.
     */
    public abstract LocalizedString toLocalizedString();
}
