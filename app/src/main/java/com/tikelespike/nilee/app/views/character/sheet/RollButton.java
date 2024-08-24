package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.game.RollBus;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.tikelespike.nilee.core.property.Property;
import com.vaadin.flow.component.button.Button;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * Button that rolls a dice expression when clicked.
 */
public class RollButton extends Button {

    /**
     * Creates a new roll button.
     *
     * @param rollProperty        the property describing which dice to roll
     * @param translationProvider translation provider used for translating and displaying UI strings
     * @param rollDescription     describes the in-game semantics of what will be rolled for, e.g. "Attack Roll"
     * @param rollBus             the "bus" on which the rolls are made.
     */
    public RollButton(@NotNull Property<DiceExpression> rollProperty, @NotNull TranslationProvider translationProvider,
                      LocalizedString rollDescription, @NotNull RollBus rollBus,
                      @NotNull PlayerCharacter characterRolling) {
        Objects.requireNonNull(rollProperty);
        Objects.requireNonNull(translationProvider);
        Objects.requireNonNull(rollBus);
        setText(rollProperty.getValue().toLocalizedString().getTranslation(translationProvider));
        addClickListener(e -> rollBus.makeRoll(rollProperty, rollDescription, characterRolling));
    }

}
