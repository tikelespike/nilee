package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AbilityScoreBox extends VerticalLayout {

    private static final String CHECK_BUTTON_CLASS = "ability-score-check-button";
    private static final String VALUE_BUTTON_CLASS = "ability-score-value-button";
    private static final String INNER_BOX_CLASS = "ability-score-inner-box";

    public AbilityScoreBox(AbilityScore abilityScore, TranslationProvider translationProvider) {
        configureStyle();

        Component skillCheckButton = createCheckButton(abilityScore, translationProvider);
        Component scoreValueButton = createScoreValueButton(abilityScore);
        Component label = createScoreLabel(abilityScore, translationProvider);

        VerticalLayout innerContentBox = createInnerContentBox(label, skillCheckButton);

        add(innerContentBox, scoreValueButton);
    }

    private void configureStyle() {
        getStyle().set("position", "relative");
        setWidth(null);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private static Button createScoreValueButton(AbilityScore abilityScore) {
        Button scoreValueButton = new Button(String.valueOf(abilityScore.getValue()));
        scoreValueButton.addClassName(VALUE_BUTTON_CLASS);
        return scoreValueButton;
    }

    private static Component createScoreLabel(AbilityScore abilityScore, TranslationProvider translationProvider) {
        Text text = new Text(abilityScore.getLongName().getTranslation(translationProvider).toUpperCase());
        Div label = new Div(text);
        label.getStyle().set("font-size", "0.8em");
        return label;
    }

    private static VerticalLayout createInnerContentBox(Component... content) {
        VerticalLayout innerContentBox = new VerticalLayout();
        innerContentBox.getThemeList().add("spacing-xs");
        innerContentBox.addClassName(INNER_BOX_CLASS);
        innerContentBox.setWidth(null);
        innerContentBox.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        innerContentBox.add(content);
        return innerContentBox;
    }

    private static Button createCheckButton(AbilityScore abilityScore, TranslationProvider translationProvider) {
        Button skillCheckButton = new RollButton(abilityScore.getCheckRoll(), translationProvider,
                t -> abilityScore.getLongName().getTranslation(t) + " Check");
        skillCheckButton.addThemeVariants(ButtonVariant.LUMO_LARGE);
        skillCheckButton.addClassName(CHECK_BUTTON_CLASS);

        // make skillCheckButton content take up as much space as needed
        skillCheckButton.setWidthFull();
        skillCheckButton.setHeightFull();
        return skillCheckButton;
    }
}
