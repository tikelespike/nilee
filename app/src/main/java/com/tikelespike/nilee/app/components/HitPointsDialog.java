package com.tikelespike.nilee.app.components;

import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.IntegerField;

public class HitPointsDialog extends Dialog {

    private final HitPoints hitPoints;

    public HitPointsDialog(HitPoints hitPoints) {
        this.hitPoints = hitPoints;
        setHeaderTitle("Hit Points");
        setMaxWidth("600px");

        FormLayout content = new FormLayout();

        H4 headingSetHP = new H4("Set HP");
        content.setColspan(headingSetHP, 3);

        IntegerField hitPointsField = new IntegerField("Current HP");
        hitPointsField.setStepButtonsVisible(true);
        hitPointsField.setMin(0);
        hitPointsField.setMax(hitPoints.getMaxHitPoints().getValue());
        hitPointsField.setValue(hitPoints.getCurrentHitPoints());

        IntegerField maxHPField = new IntegerField("Max HP");
        maxHPField.setReadOnly(true);
        maxHPField.setValue(hitPoints.getMaxHitPoints().getValue());

        IntegerField maxHPOverrideField = new IntegerField("Max HP Override");
        maxHPOverrideField.setStepButtonsVisible(true);
        maxHPOverrideField.setMin(0);

        IntegerField tempHPField = new IntegerField("Temp HP");
        tempHPField.setStepButtonsVisible(true);
        tempHPField.setMin(0);
        content.setColspan(tempHPField, 3);


        H4 headingDamageHeal = new H4("Healing/Damage");
        content.setColspan(headingDamageHeal, 3);

        Button damageButton = new Button("Damage");
        damageButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        IntegerField deltaField = new IntegerField("Heal/Damage amount");
        deltaField.setStepButtonsVisible(true);
        deltaField.setMin(0);

        Button healButton = new Button("Heal");
        healButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);



        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("400px", 3));

        content.add(headingDamageHeal, damageButton, deltaField, healButton);
        content.add(headingSetHP, hitPointsField, maxHPField, maxHPOverrideField, tempHPField);

        add(content);


        Button closeButton = new Button("Close");
        closeButton.addClickListener(e -> close());

        getFooter().add(closeButton);
    }

}
