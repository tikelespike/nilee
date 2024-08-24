package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Fired when a roll is made. Contains details about the roll and its results. The roll is made on the {@link RollBus},
 * which will notify all listeners of the roll.
 */
public class RollEvent extends Event {

    private final LocalizedString description;
    private final DiceExpression[] computationSteps;
    private final PlayerCharacter characterRolling;
    private final User userRolling;


    /**
     * @param description      a localized string describing what type of roll was made
     * @param characterRolling if the roll is made by a player character, the character rolling. May be null.
     * @param userRolling      user that initiated the roll
     * @param computationSteps all steps from the initial roll to the final result, including the original roll expression,
     *                         intermediate results (if any) and the final result (in that order). May not be empty.
     *                         If the length is 1, the result is assumed to be the original roll expression.
     */
    public RollEvent(LocalizedString description, PlayerCharacter characterRolling, User userRolling,
                     @NotNull DiceExpression... computationSteps) {
        this.description = Objects.requireNonNull(description);
        this.characterRolling = characterRolling;
        this.userRolling = Objects.requireNonNull(userRolling);
        this.computationSteps = Objects.requireNonNull(computationSteps);
    }

    /**
     * @return the final result of evaluating the dice expression
     */
    public int getResult() {
        return this.computationSteps[this.computationSteps.length - 1].evaluate();
    }

    /**
     * @return the dice expression that was rolled
     */
    public DiceExpression getRoll() {
        return this.computationSteps[0];
    }

    public DiceExpression[] getComputationSteps() {
        return this.computationSteps.clone();
    }

    /**
     * @return a localized string describing what type of roll was made
     */
    public LocalizedString getDescription() {
        return description;
    }

    /**
     * @return the player character that made the roll, or null if roll not associated with character
     */
    public PlayerCharacter getCharacterRolling() {
        return characterRolling;
    }

    /**
     * @return the user that made the roll
     */
    public User getUserRolling() {
        return userRolling;
    }
}
