package com.tikelespike.nilee.app.components;

import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.IntegerField;

public class HitPointsDialog extends Dialog {

    private final HitPoints hitPoints;

    public HitPointsDialog(HitPoints hitPoints) {
        this.hitPoints = hitPoints;
        setHeaderTitle(getTranslation("character_editor.hit_points.title"));
        setMaxWidth("600px");

        FormLayout content = createContent();
        add(content);

        Button closeButton = new Button("Close");
        closeButton.addClickListener(e -> close());

        getFooter().add(closeButton);
    }

    private FormLayout createContent() {
        FormLayout content = new FormLayout();
        content.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("400px", 3));

        createAndAddHealDamageSection(content);
        createAndAddSetHPSection(content);
        return content;
    }

    private void createAndAddSetHPSection(FormLayout content) {
        H4 headingSetHP = new H4(getTranslation("character_editor.hit_points.section.set_hp.heading"));
        content.setColspan(headingSetHP, 3);

        IntegerField hitPointsField = new IntegerField(getTranslation("character_editor.hit_points.section.set_hp.current"));
        hitPointsField.setStepButtonsVisible(true);
        hitPointsField.setMin(0);
        hitPointsField.setMax(hitPoints.getMaxHitPoints().getValue());
        hitPointsField.setValue(hitPoints.getCurrentHitPoints());

        IntegerField maxHPField = new IntegerField(getTranslation("character_editor.hit_points.section.set_hp.max"));
        maxHPField.setReadOnly(true);
        maxHPField.setValue(hitPoints.getMaxHitPoints().getValue());

        IntegerField maxHPOverrideField = new IntegerField(getTranslation("character_editor.hit_points.section.set_hp.max_override"));
        maxHPOverrideField.setStepButtonsVisible(true);
        maxHPOverrideField.setMin(0);

        IntegerField tempHPField = new IntegerField(getTranslation("character_editor.hit_points.section.set_hp.temp"));
        tempHPField.setStepButtonsVisible(true);
        tempHPField.setMin(0);
        content.setColspan(tempHPField, 3);

        content.add(headingSetHP, hitPointsField, maxHPField, maxHPOverrideField, tempHPField);
    }

    private void createAndAddHealDamageSection(FormLayout content) {
        H4 headingDamageHeal = new H4(getTranslation("character_editor.hit_points.section.heal_damage.heading"));
        content.setColspan(headingDamageHeal, 3);

        Button damageButton = new Button(getTranslation("character_editor.hit_points.section.heal_damage.damage"));
        damageButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);

        IntegerField deltaField = new IntegerField(getTranslation("character_editor.hit_points.section.heal_damage.amount"));
        deltaField.setStepButtonsVisible(true);
        deltaField.setMin(0);

        Button healButton = new Button(getTranslation("character_editor.hit_points.section.heal_damage.heal"));
        healButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

        content.add(headingDamageHeal, damageButton, deltaField, healButton);
    }

}
