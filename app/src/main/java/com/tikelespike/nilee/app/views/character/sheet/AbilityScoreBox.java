package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AbilityScoreBox extends VerticalLayout {

    private final AbilityScore abilityScore;
    private final TranslationProvider translationProvider;

    public AbilityScoreBox(AbilityScore abilityScore, TranslationProvider translationProvider) {
        this.abilityScore = abilityScore;
        this.translationProvider = translationProvider;

        Button skillCheckButton = new RollButton(abilityScore.getCheckRoll(), translationProvider,
                t -> abilityScore.getLongName().getTranslation(t) + " Check");

        add(skillCheckButton);
    }
}
