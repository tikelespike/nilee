package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.i18n.LocalizedString;

public class RollEvent extends Event {

    private final DiceExpression roll;
    private final DiceExpression partialResult;
    private final int result;
    private final LocalizedString description;


    public RollEvent(DiceExpression roll, DiceExpression partialResult, int result, LocalizedString description) {
        this.roll = roll;
        this.partialResult = partialResult;
        this.result = result;
        this.description = description;
    }

    public int getResult() {
        return result;
    }

    public DiceExpression getRoll() {
        return roll;
    }

    public DiceExpression getPartialResult() {
        return partialResult;
    }

    public LocalizedString getDescription() {
        return description;
    }
}
