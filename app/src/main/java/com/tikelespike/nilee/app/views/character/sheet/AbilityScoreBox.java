package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AbilityScoreBox extends VerticalLayout {

    private final AbilityScore abilityScore;
    private final TranslationProvider translationProvider;

    public AbilityScoreBox(AbilityScore abilityScore, TranslationProvider translationProvider) {
        this.abilityScore = abilityScore;
        this.translationProvider = translationProvider;

        Button skillCheckButton = new RollButton(abilityScore.getCheckRoll(), translationProvider,
                t -> abilityScore.getLongName().getTranslation(t) + " Check");
        skillCheckButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        skillCheckButton.setMinWidth("130px");
        skillCheckButton.setMinHeight("100px");
        // increase font size
        skillCheckButton.getStyle().set("font-size", "1.75em");

        Button scoreValueButton = new Button(String.valueOf(abilityScore.getValue()));
        scoreValueButton.getStyle().set("border-radius", "50%");

        getStyle().set("border", "1px");
        getStyle().set("border-radius", "var(--lumo-border-radius-l)");
        getStyle().set("border-style", "solid");
        getStyle().set("border-color", "var(--lumo-contrast-30pct)");
        getThemeList().add("spacing-xs");

        setWidth(null);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        Text label = new Text(abilityScore.getLongName().getTranslation(translationProvider).toUpperCase());

        add(label, skillCheckButton, scoreValueButton);
    }
}
