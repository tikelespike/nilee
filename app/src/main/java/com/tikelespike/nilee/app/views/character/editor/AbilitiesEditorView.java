package com.tikelespike.nilee.app.views.character.editor;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class AbilitiesEditorView extends FormLayout {

    private final PlayerCharacter pc;
    private final Binder<PlayerCharacter> binder;

    public AbilitiesEditorView(PlayerCharacter pc) {
        this.pc = pc;

        IntegerField strField = createStatField(getTranslation("character_editor.abilities.str.label"));
        IntegerField dexField = createStatField(getTranslation("character_editor.abilities.dex.label"));
        IntegerField conField = createStatField(getTranslation("character_editor.abilities.con.label"));
        IntegerField intField = createStatField(getTranslation("character_editor.abilities.int.label"));
        IntegerField wisField = createStatField(getTranslation("character_editor.abilities.wis.label"));
        IntegerField chaField = createStatField(getTranslation("character_editor.abilities.cha.label"));

        setResponsiveSteps(new ResponsiveStep("0", 6));
        add(strField, dexField, conField, intField, wisField, chaField);

        // maps the UI fields to the attributes object
        binder = new Binder<>(PlayerCharacter.class);
        binder.forField(strField).bind(c -> c.getAbilityScores().getStrength().getDefaultBaseValue(),
                (c, v) -> pc.getAbilityScores().getStrength().setDefaultBaseValue(v));
        binder.forField(dexField).bind(c -> c.getAbilityScores().getDexterity().getDefaultBaseValue(),
                (c, v) -> pc.getAbilityScores().getDexterity().setDefaultBaseValue(v));
        binder.forField(conField).bind(c -> c.getAbilityScores().getConstitution().getDefaultBaseValue(),
                (c, v) -> pc.getAbilityScores().getConstitution().setDefaultBaseValue(v));
        binder.forField(intField).bind(c -> c.getAbilityScores().getIntelligence().getDefaultBaseValue(),
                (c, v) -> pc.getAbilityScores().getIntelligence().setDefaultBaseValue(v));
        binder.forField(wisField).bind(c -> c.getAbilityScores().getWisdom().getDefaultBaseValue(),
                (c, v) -> pc.getAbilityScores().getWisdom().setDefaultBaseValue(v));
        binder.forField(chaField).bind(c -> c.getAbilityScores().getCharisma().getDefaultBaseValue(),
                (c, v) -> pc.getAbilityScores().getCharisma().setDefaultBaseValue(v));

        binder.readBean(pc);
    }

    public void update() {
        try {
            binder.writeBean(pc);
        } catch (ValidationException e) {
            throw new RuntimeException(e);
        }
    }

    private IntegerField createStatField(String name) {
        IntegerField field = new IntegerField(name);
        field.setStepButtonsVisible(true);
        field.setMin(1);
        field.setMax(20);
        field.setValue(10);
        return field;
    }
}
