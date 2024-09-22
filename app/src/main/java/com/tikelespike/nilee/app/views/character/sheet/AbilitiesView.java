package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.game.RollBus;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Displays the {@link AbilityScore AbilityScores} of a player character. This includes buttons to roll checks for each
 * ability score.
 */
public class AbilitiesView extends VerticalLayout {

    /**
     * Creates a new AbilitiesView.
     *
     * @param rollBus the bus on which ability checks are rolled
     * @param translationProvider provides translations for UI display
     * @param pc the player character whose ability scores are displayed
     */
    public AbilitiesView(RollBus rollBus, TranslationProvider translationProvider, PlayerCharacter pc) {
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        HorizontalLayout abilityScoreBoxes = createMainScoreBoxes(rollBus, translationProvider, pc);
        add(abilityScoreBoxes);

        add(new Hr());

        // TODO: Saving throws, passive senses
    }

    private static HorizontalLayout createMainScoreBoxes(RollBus rollBus, TranslationProvider translationProvider,
                                                         PlayerCharacter pc) {
        HorizontalLayout abilityScoreBoxes = new HorizontalLayout();
        for (AbilityScore score : pc.getAbilityScores().getAll()) {
            abilityScoreBoxes.add(new AbilityScoreBox(score, translationProvider, rollBus, pc));
        }
        abilityScoreBoxes.setJustifyContentMode(JustifyContentMode.CENTER);
        return abilityScoreBoxes;
    }
}
