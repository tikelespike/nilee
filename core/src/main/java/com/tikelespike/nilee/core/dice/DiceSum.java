package com.tikelespike.nilee.core.dice;

import com.tikelespike.nilee.core.i18n.LocalizedString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Multiple dice expressions added together. For example, "2d6 + 1d4 + 3" is a dice sum with three summands.
 */
public class DiceSum extends DiceExpression {

    private final List<DiceExpression> summands = new ArrayList<>();

    /**
     * Creates a new dice sum.
     *
     * @param summands the summands of this dice sum.
     */
    public DiceSum(DiceExpression... summands) {
        this.summands.addAll(Arrays.asList(summands));
    }

    /**
     * Creates a new dice sum.
     *
     * @param summands the summands of this dice sum.
     */
    public DiceSum(List<DiceExpression> summands) {
        this.summands.addAll(summands);
    }

    /**
     * Adds the given summands to this dice sum.
     *
     * @param summands the summands to add to this dice sum.
     */
    public void addSummands(DiceExpression... summands) {
        this.summands.addAll(Arrays.asList(summands));
    }

    /**
     * Returns a copy of the summands of this dice sum.
     *
     * @return copy of the summands of this dice sum.
     */
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
                    summands.stream().map(DiceExpression::toLocalizedString).map(l -> l.getTranslation(t))
                            .map(s -> s.startsWith("-") ? s : "+ " + s).toArray(String[]::new));

            if (join.startsWith("+ ")) {
                join = join.substring(2);
            }

            return join;
        };
    }
}