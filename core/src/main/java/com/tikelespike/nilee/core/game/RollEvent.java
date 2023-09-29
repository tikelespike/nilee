package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.i18n.LocalizedString;

/**
 * Fired when a roll is made. Contains details about the roll and its results. The roll is made on the {@link RollBus},
 * which will notify all listeners of the roll.
 */
public class RollEvent extends Event {

    private final DiceExpression roll;
    private final DiceExpression partialResult;
    private final int result;
    private final LocalizedString description;


    /**
     * @param roll          the dice expression that was rolled
     * @param partialResult the expression with all dice values rolled, but without evaluating the operands (usually, summing different dice)
     * @param result        the final result of evaluating the dice expression
     * @param description   a localized string describing what type of roll was made
     */
    public RollEvent(DiceExpression roll, DiceExpression partialResult, int result, LocalizedString description) {
        this.roll = roll;
        this.partialResult = partialResult;
        this.result = result;
        this.description = description;
    }

    /**
     * @return the final result of evaluating the dice expression
     */
    public int getResult() {
        return result;
    }

    /**
     * @return the dice expression that was rolled
     */
    public DiceExpression getRoll() {
        return roll;
    }

    /**
     * @return the expression with all dice values rolled, but without evaluating the operands (usually, summing different dice)
     */
    public DiceExpression getPartialResult() {
        return partialResult;
    }

    /**
     * @return a localized string describing what type of roll was made
     */
    public LocalizedString getDescription() {
        return description;
    }
}
