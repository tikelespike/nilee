package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.views.character.sheet.dice.RollManager;
import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.tikelespike.nilee.core.property.Property;
import com.vaadin.flow.component.button.Button;

import javax.validation.constraints.NotNull;
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
     * @param rollManager         the "bus" on which the rolls are made. It is recommended to use only one roll manager per UI.
     */
    public RollButton(@NotNull Property<DiceExpression> rollProperty, @NotNull TranslationProvider translationProvider,
                      LocalizedString rollDescription, @NotNull RollManager rollManager) {
        Objects.requireNonNull(rollProperty);
        Objects.requireNonNull(translationProvider);
        Objects.requireNonNull(rollManager);
        setText(rollProperty.getValue().toLocalizedString(true).getTranslation(translationProvider));
        addClickListener(e -> rollManager.makeRoll(rollProperty, rollDescription));
    }

}
