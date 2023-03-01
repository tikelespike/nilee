package com.tikelespike.nilee.views.character.editor;

import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.property.SDCIWCAttributes;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

import java.awt.*;

public class AbilitiesEditorView extends FormLayout {

    private final SDCIWCAttributes attributes;
    private final Binder<SDCIWCAttributes> binder;

    public AbilitiesEditorView(SDCIWCAttributes attributes) {
        this.attributes = attributes;

        IntegerField strField = createStatField("Base Strength");
        IntegerField dexField = createStatField("Base Dexterity");
        IntegerField conField = createStatField("Base Constitution");
        IntegerField intField = createStatField("Base Intelligence");
        IntegerField wisField = createStatField("Base Wisdom");
        IntegerField chaField = createStatField("Base Charisma");

        setResponsiveSteps(new ResponsiveStep("0", 6));
        add(strField, dexField, conField, intField, wisField, chaField);

        // create a binder mapping the field values to the pc
        binder = new Binder<>(SDCIWCAttributes.class);
        binder.forField(strField).bind(SDCIWCAttributes::getBaseStrength, SDCIWCAttributes::setBaseStrength);

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
