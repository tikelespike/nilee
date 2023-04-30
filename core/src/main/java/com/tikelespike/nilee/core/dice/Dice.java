package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

import java.util.Objects;

public class Dice extends DiceExpression {

    private int sides;
    private int diceCount;

    public Dice(int diceCount, int sides) {
        this.sides = sides;
        this.diceCount = diceCount;
    }

    public Dice(int sides) {
        this(1, sides);
    }

    @Override
    public int evaluate() {
        int result = 0;
        for (int i = 0; i < Math.abs(diceCount); i++) {
            result += (int) Math.signum(diceCount) * ((Math.random() * sides) + 1);
        }
        return result;
    }

    @Override
    public DiceExpression evaluatePartially() {
        return new DiceConstant(evaluate());
    }

    public int getSides() {
        return sides;
    }

    public void setSides(int sides) {
        this.sides = sides;
    }

    public int getDiceCount() {
        return diceCount;
    }

    public void setDiceCount(int diceCount) {
        this.diceCount = diceCount;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dice that = (Dice) o;
        return sides == that.sides && diceCount == that.diceCount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sides, diceCount);
    }
}