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
    public DiceExpression evaluatePartially() {
        DiceSum result = new DiceSum();
        for (DiceExpression summand : summands) {
            result.addSummands(summand.evaluatePartially());
        }
        return result;
    }

    @Override
    public String toString() {
        return String.join(" + ", summands.stream().map(d -> "(" + d.toString() + ")").toArray(String[]::new));
    }

    @Override
    public LocalizedString toLocalizedString() {
        return t -> {
            String join = String.join(" ",
                    summands.stream()
                            .map(DiceExpression::toLocalizedString)
                            .map(l -> l.getTranslation(t))
                            .map(s -> s.startsWith("-") ? s : "+ " + s)
                            .toArray(String[]::new));

            if (join.startsWith("+ ")) {
                join = join.substring(2);
            }

            return join;
        };
    }
}