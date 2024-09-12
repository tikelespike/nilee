package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

import java.util.Objects;

/**
 * A number of dice with the same number of sides each. Represents what is commonly expressed as, for example, "2d8",
 * meaning "roll two eight-sided dice". The results of each die are summed.
 *
 * @see <a href="https://www.dndbeyond.com/sources/phb/introduction#GameDice">Dice notation</a>
 */
public class Dice extends DiceExpression {

    private final int sides;
    private final int diceCount;

    /**
     * Create a dice object representing the expression "<i>[diceCount]</i>d<i>[sides]</i>".
     *
     * @param diceCount the number of dice. Must be greater than zero.
     * @param sides the number of sides of each die. If negative, treated as
     *         {@code -(Dice(diceCount, -sides))}.
     */
    public Dice(int diceCount, int sides) {
        this.sides = sides;
        this.diceCount = diceCount;
    }

    /**
     * Convenience constructor for a single die. Creates a dice object representing the expression "1d<i>[sides]</i>".
     *
     * @param sides the number of sides of each die. If negative, treated as {@code -(Dice(-sides))}.
     */
    public Dice(int sides) {
        this(1, sides);
    }

    @Override
    public int evaluate() {
        int result = 0;
        for (int i = 0; i < Math.abs(diceCount); i++) {
            result += (int) ((int) Math.signum(diceCount) * ((Math.random() * sides) + 1));
        }
        return result;
    }

    @Override
    public DiceExpression evaluatePartially() {
        return new DiceConstant(evaluate());
    }


    @Override
    public String toString() {
        return diceCount + "d" + sides;
    }

    @Override
    public LocalizedString toLocalizedString() {
        return t -> t.translate("dice.atomic_dice_expression", (diceCount < 0 ? "- " : "") + Math.abs(diceCount),
                sides);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dice that = (Dice) o;
        return sides == that.sides && diceCount == that.diceCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sides, diceCount);
    }
}