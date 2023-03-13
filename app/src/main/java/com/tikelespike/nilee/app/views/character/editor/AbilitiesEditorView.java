package com.tikelespike.nilee.app.views.character.editor;

import com.tikelespike.nilee.core.data.entity.property.AbilityScores;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

public class AbilitiesEditorView extends FormLayout {

    private final AbilityScores attributes;
    private final Binder<AbilityScores> binder;

    public AbilitiesEditorView(AbilityScores attributes) {
        this.attributes = attributes;

        IntegerField strField = createStatField(getTranslation("character_editor.abilities.str.label"));
        IntegerField dexField = createStatField(getTranslation("character_editor.abilities.dex.label"));
        IntegerField conField = createStatField(getTranslation("character_editor.abilities.con.label"));
        IntegerField intField = createStatField(getTranslation("character_editor.abilities.int.label"));
        IntegerField wisField = createStatField(getTranslation("character_editor.abilities.wis.label"));
        IntegerField chaField = createStatField(getTranslation("character_editor.abilities.cha.label"));

        setResponsiveSteps(new ResponsiveStep("0", 6));
        add(strField, dexField, conField, intField, wisField, chaField);

        // maps the UI fields to the attributes object
        binder = new Binder<>(AbilityScores.class);
        binder.forField(strField).bind(scores -> scores.getStrength().getBaseValue(), (scores, v) -> scores.getStrength().setBaseValue(v));

        binder.readBean(attributes);
    }

    public void update() {
        try {
            binder.writeBean(attributes);
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
