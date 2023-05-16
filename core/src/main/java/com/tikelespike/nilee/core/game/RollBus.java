package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.Property;

import javax.validation.constraints.NotNull;
import java.util.Objects;

public class RollBus {

    private final EventBus eventBus = new EventBus();

    public int makeRoll(@NotNull Property<DiceExpression> rollProperty, LocalizedString rollDescription) {
        Objects.requireNonNull(rollProperty);

        DiceExpression roll = rollProperty.getValue();
        DiceExpression partialResult = roll.evaluatePartially();
        int result = partialResult.evaluate();

        eventBus.fireEvent(new RollEvent(roll, partialResult, result, rollDescription));

        return result;
    }

    public Registration registerRollListener(@NotNull EventListener<? super RollEvent> listener) {
        return eventBus.registerListener(RollEvent.class, listener);
    }

}
