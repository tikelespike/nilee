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
    int evaluate() {
        return summands.stream().map(DiceExpression::evaluate).reduce(0, Integer::sum);
    }

    @Override
    public String toString() {
        return String.join(" + ", summands.stream().map(Object::toString).toArray(String[]::new));
    }

    @Override
    LocalizedString toLocalizedString() {
        return t -> String.join(" " + t.translate("dice.sum.operator") + " ",
                summands.stream().map(DiceExpression::toLocalizedString).map(l -> l.getTranslation(t)).toArray(String[]::new));
    }
}