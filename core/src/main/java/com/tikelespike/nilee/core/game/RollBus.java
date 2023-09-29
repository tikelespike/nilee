package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.Property;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A "channel" on which rolls are made by calling {@link #makeRoll(Property, LocalizedString)}. Multiple application
 * instances might share a channel, for example if multiple players are playing together and want to view the same
 * dice rolls.
 */
public class RollBus {

    private final EventBus eventBus = new EventBus();

    /**
     * Make a roll on this roll bus. The type of dice to roll is determined by the property given,
     * and all listeners will be notified of the roll.
     *
     * @param rollProperty    the property describing which dice to roll
     * @param rollDescription describes the in-game semantics of what will be rolled for, e.g. "Attack Roll"
     * @return the final result of the roll
     */
    public int makeRoll(@NotNull Property<DiceExpression> rollProperty, LocalizedString rollDescription) {
        Objects.requireNonNull(rollProperty);

        DiceExpression roll = rollProperty.getValue();
        DiceExpression partialResult = roll.evaluatePartially();
        int result = partialResult.evaluate();

        eventBus.fireEvent(new RollEvent(roll, partialResult, result, rollDescription));

        return result;
    }

    /**
     * Registers a listener to be notified of all rolls made on this roll bus.
     *
     * @param listener the listener to register
     * @return a registration that can be used to unregister the listener
     */
    public Registration registerRollListener(@NotNull EventListener<? super RollEvent> listener) {
        return eventBus.registerListener(RollEvent.class, listener);
    }

}
