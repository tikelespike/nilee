package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiceSum extends DiceExpression {

    private final List<DiceExpression> summands = new ArrayList<>();

    public DiceSum(DiceExpression... summands) {
        this.summands.addAll(Arrays.asList(summands));
    }

    public DiceSum(List<DiceExpression> summands) {
        this.summands.addAll(summands);
    }

    public void addSummands(DiceExpression... summands) {
        this.summands.addAll(Arrays.asList(summands));
    }

    public List<DiceExpression> getSummands() {
        return new ArrayList<>(summands);
    }

    @Override
    public int evaluate() {
        return summands.stream().map(DiceExpression::evaluate).reduce(0, Integer::sum);
    }

    @Override
    public String toString() {
        return String.join(" + ", summands.stream().map(Object::toString).toArray(String[]::new));
    }

    @Override
    public LocalizedString toLocalizedString() {
        // special case 1d20 + const for attack rolls etc.
        if (summands.size() == 2
                && summands.get(0) instanceof Dice dice && dice.getDiceCount() == 1 && dice.getSides() == 20
                && summands.get(1) instanceof DiceConstant constant) {
            return t -> t.translate("dice.sum.operator") + " " + constant.toLocalizedString().getTranslation(t);
        }

        return t -> String.join(" " + t.translate("dice.sum.operator") + " ",
                summands.stream().map(DiceExpression::toLocalizedString).map(l -> l.getTranslation(t)).toArray(String[]::new));
    }
}