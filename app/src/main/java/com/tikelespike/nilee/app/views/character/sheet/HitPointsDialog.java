package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
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

/**
 * Dialog for editing hit points. Consists of a form for editing the hit points as well as taking damage and healing, a
 * save button and a discard button.
 */
public class HitPointsDialog extends Dialog {

    private static final String MAX_WIDTH = "700px";
    private static final String MIN_WIDTH_3COL = "400px";
    private static final int COLUMNS = 3;

    private final HitPoints hitPoints;
    private final Set<Registration> registrations = new HashSet<>();
    private final CharacterSaver characterSaver;


    /**
     * Creates a new dialog for editing hit points.
     *
     * @param hitPoints the hit points to edit
     * @param characterSaver used to save the corresponding character upon clicking the save button of this
     *         dialog
     */
    public HitPointsDialog(HitPoints hitPoints, CharacterSaver characterSaver) {
        this.hitPoints = hitPoints;
        this.characterSaver = characterSaver;

        setHeaderTitle(getTranslation("character_sheet.hit_points.title"));
        setMaxWidth(MAX_WIDTH);

        FormLayout content = createContent();
        add(content);

        Button saveButton = createSaveButton();
        Button discardButton = new Button(getTranslation("generic.discard"), e -> cleanupAndClose());
        addDialogCloseActionListener(e -> cleanupAndClose());

        getFooter().add(discardButton, saveButton);
    }

    private Button createSaveButton() {
        Button saveButton = new Button(getTranslation("generic.save"));
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        Consumer<CharacterSaver.SaveResult> onSave = result -> {
            if (result == CharacterSaver.SaveResult.SAVED || result == CharacterSaver.SaveResult.CHANGES_DISCARDED) {
                cleanupAndClose();
            }
        };
        saveButton.addClickListener(e -> characterSaver.save(onSave));
        return saveButton;
    }

    private void cleanupAndClose() {
        characterSaver.discard();
        registrations.forEach(Registration::unregister);
        close();
    }

    private FormLayout createContent() {
        FormLayout content = new FormLayout();
        content.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep(MIN_WIDTH_3COL, COLUMNS));

        createAndAddHealDamageSection(content);
        createAndAddSetHPSection(content);
        return content;
    }

    private void createAndAddSetHPSection(FormLayout content) {
        H4 headingSetHP = new H4(getTranslation("character_sheet.hit_points.section.set_hp.heading"));
        content.setColspan(headingSetHP, COLUMNS);

        IntegerField hitPointsField = createHitPointsField();
        IntegerField maxHPField = createMaxHPField(hitPointsField);
        Checkbox maxHPOverrideCheckbox = createMaxHPOverrideCheckbox(maxHPField);
        IntegerField tempHPField = createTempHPField();

        content.add(headingSetHP, hitPointsField, maxHPField, maxHPOverrideCheckbox, tempHPField);
    }

    private IntegerField createTempHPField() {
        IntegerField tempHPField = new IntegerField(getTranslation("character_sheet.hit_points.section.set_hp.temp"));
        tempHPField.setStepButtonsVisible(true);
        tempHPField.setMin(0);
        tempHPField.setValue(hitPoints.getTemporaryHitPoints());
        tempHPField.addValueChangeListener(
                e -> hitPoints.setTemporaryHitPoints(e.getValue() == null ? 0 : e.getValue()));
        registrations.add(hitPoints.registerTempHPChangeListener(e -> tempHPField.setValue(e.getNewValue())));
        return tempHPField;
    }

    private Checkbox createMaxHPOverrideCheckbox(IntegerField maxHPField) {
        Checkbox maxHPOverrideCheckbox =
                new Checkbox(getTranslation("character_sheet.hit_points.section.set_hp.override_max"));
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
        return maxHPOverrideCheckbox;
    }

    private IntegerField createMaxHPField(IntegerField hitPointsField) {
        IntegerField maxHPField = new IntegerField(getTranslation("character_sheet.hit_points.section.set_hp.max"));
        maxHPField.setReadOnly(!hitPoints.getMaxHitPoints().isOverridden());
        maxHPField.setValue(hitPoints.getMaxHitPoints().getValue());
        maxHPField.setStepButtonsVisible(true);
        maxHPField.setMin(0);
        maxHPField.setRequiredIndicatorVisible(true);
        maxHPField.setErrorMessage(getTranslation("error.empty_field"));
        maxHPField.addValueChangeListener(e -> {
            if (e.getValue() == null) {
                return;
            }
            hitPointsField.setMax(e.getValue());
            if (!maxHPField.isReadOnly()) {
                hitPoints.getMaxHitPoints().setOverride(e.getValue());
            }
        });
        registrations.add(
                hitPoints.getMaxHitPoints().addValueChangeListener(e -> maxHPField.setValue(e.getNewValue())));
        return maxHPField;
    }

    private IntegerField createHitPointsField() {
        IntegerField hitPointsField =
                new IntegerField(getTranslation("character_sheet.hit_points.section.set_hp.current"));
        hitPointsField.setStepButtonsVisible(true);
        hitPointsField.setMin(0);
        hitPointsField.setMax(hitPoints.getMaxHitPoints().getValue());
        hitPointsField.setValue(hitPoints.getCurrentHitPoints());
        hitPointsField.addValueChangeListener(
                e -> hitPoints.setCurrentHitPoints(e.getValue() == null ? 0 : e.getValue()));
        registrations.add(hitPoints.registerCurrentHPChangeListener(e -> hitPointsField.setValue(e.getNewValue())));
        return hitPointsField;
    }

    private void createAndAddHealDamageSection(FormLayout content) {
        H4 headingDamageHeal = new H4(getTranslation("character_sheet.hit_points.section.heal_damage.heading"));
        content.setColspan(headingDamageHeal, COLUMNS);

        IntegerField deltaField =
                new IntegerField(getTranslation("character_sheet.hit_points.section.heal_damage.amount"));
        deltaField.setStepButtonsVisible(true);
        deltaField.setMin(0);
        deltaField.setValue(1);
        deltaField.addValueChangeListener(e -> {
            if (e.getValue() == null) {
                deltaField.setValue(1);
            }
        });

        Button damageButton = new Button(getTranslation("character_sheet.hit_points.section.heal_damage.damage"));
        damageButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        damageButton.addClickListener(e -> {
            hitPoints.takeDamage(deltaField.getValue() == null ? 0 : deltaField.getValue());
            deltaField.setValue(deltaField.getEmptyValue());
        });

        Button healButton = new Button(getTranslation("character_sheet.hit_points.section.heal_damage.heal"));
        healButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
        healButton.addClickListener(e -> {
            hitPoints.heal(deltaField.getValue() == null ? 0 : deltaField.getValue());
            deltaField.setValue(deltaField.getEmptyValue());
        });

        content.add(headingDamageHeal, damageButton, deltaField, healButton);
    }
}
