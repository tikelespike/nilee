package com.tikelespike.nilee.app.components;

import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.tikelespike.nilee.core.events.Registration;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.textfield.IntegerField;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class HitPointsDialog extends Dialog {

    private final HitPoints hitPoints;
    private final CharacterSaver characterSaver;

    private PlayerCharacter playerCharacter;

    private Set<Registration> registrations = new HashSet<>();

    public HitPointsDialog(PlayerCharacter playerCharacter, PlayerCharacterService characterService, User currentUser) {
        this.playerCharacter = playerCharacter;
        this.hitPoints = playerCharacter.getHitPoints();
        this.characterSaver = new CharacterSaver(playerCharacter, characterService, currentUser);

        setHeaderTitle(getTranslation("character_editor.hit_points.title"));
        setMaxWidth("700px");

        FormLayout content = createContent();
        add(content);

        Button saveButton = new Button(getTranslation("generic.save"));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Consumer<CharacterSaver.SaveResult> onSave = result -> {
            if (result == CharacterSaver.SaveResult.SAVED || result == CharacterSaver.SaveResult.CHANGES_DISCARDED) {
                registrations.forEach(Registration::unregister);
                close();
            }
        };
        saveButton.addClickListener(e -> {
            characterSaver.save(onSave);
        });

        Button discardButton = new Button(getTranslation("generic.discard"));
        discardButton.addClickListener(e -> {
            characterSaver.discard();
            registrations.forEach(Registration::unregister);
            close();
        });

        addDialogCloseActionListener(e -> {
            characterSaver.discard();
            registrations.forEach(Registration::unregister);
            close();
        });

        getFooter().add(discardButton, saveButton);
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
        hitPointsField.addValueChangeListener(e -> hitPoints.setCurrentHitPoints(e.getValue() == null ? 0 : e.getValue()));
        registrations.add(hitPoints.registerCurrentHPChangeListener(e -> hitPointsField.setValue(e.getNewValue())));

        IntegerField maxHPField = new IntegerField(getTranslation("character_editor.hit_points.section.set_hp.max"));
        maxHPField.setReadOnly(!hitPoints.getMaxHitPoints().isOverridden());
        maxHPField.setValue(hitPoints.getMaxHitPoints().getValue());
        maxHPField.setStepButtonsVisible(true);
        maxHPField.setMin(0);
        maxHPField.setRequiredIndicatorVisible(true);
        maxHPField.setErrorMessage(getTranslation("error.empty_field"));
        maxHPField.addValueChangeListener(e -> {
            if (e.getValue() == null) return;
            hitPointsField.setMax(e.getValue());
            if (!maxHPField.isReadOnly()) {
                hitPoints.getMaxHitPoints().setOverride(e.getValue());
            }
        });
        registrations.add(hitPoints.getMaxHitPoints().addValueChangeListener(e -> maxHPField.setValue(e.getNewValue())));

        Checkbox maxHPOverrideCheckbox = new Checkbox(getTranslation("character_editor.hit_points.section.set_hp.override_max"));
        maxHPOverrideCheckbox.setValue(hitPoints.getMaxHitPoints().isOverridden());
        maxHPOverrideCheckbox.addValueChangeListener(e -> {
            maxHPField.setReadOnly(!e.getValue());
            if (e.getValue()) {
                hitPoints.getMaxHitPoints().setOverride(hitPoints.getMaxHitPoints().getValue());
            } else {
                hitPoints.getMaxHitPoints().removeOverride();
            }
            maxHPField.setValue(hitPoints.getMaxHitPoints().getValue());
        });

        IntegerField tempHPField = new IntegerField(getTranslation("character_editor.hit_points.section.set_hp.temp"));
        tempHPField.setStepButtonsVisible(true);
        tempHPField.setMin(0);
        tempHPField.setValue(hitPoints.getTemporaryHitPoints());
        tempHPField.addValueChangeListener(e -> hitPoints.setTemporaryHitPoints(e.getValue() == null ? 0 : e.getValue()));
        registrations.add(hitPoints.registerTempHPChangeListener(e -> tempHPField.setValue(e.getNewValue())));

        content.add(headingSetHP, hitPointsField, maxHPField, maxHPOverrideCheckbox, tempHPField);
    }

    private void createAndAddHealDamageSection(FormLayout content) {
        H4 headingDamageHeal = new H4(getTranslation("character_editor.hit_points.section.heal_damage.heading"));
        content.setColspan(headingDamageHeal, 3);

        IntegerField deltaField = new IntegerField(getTranslation("character_editor.hit_points.section.heal_damage.amount"));
        deltaField.setStepButtonsVisible(true);
        deltaField.setMin(0);

        Button damageButton = new Button(getTranslation("character_editor.hit_points.section.heal_damage.damage"));
        damageButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        damageButton.addClickListener(e -> {
            hitPoints.takeDamage(deltaField.getValue());
            deltaField.setValue(deltaField.getEmptyValue());
        });

        Button healButton = new Button(getTranslation("character_editor.hit_points.section.heal_damage.heal"));
        healButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        healButton.addClickListener(e -> {
            hitPoints.heal(deltaField.getValue());
            deltaField.setValue(deltaField.getEmptyValue());
        });

        content.add(headingDamageHeal, damageButton, deltaField, healButton);
    }
}
