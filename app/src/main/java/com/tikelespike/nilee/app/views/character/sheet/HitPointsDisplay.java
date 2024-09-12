package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;

/**
 * A minimalistic display of a character's hit points. Consists of a button that displays the current hit points and
 * temporary hit points numerically, a progress bar for the current hit points and a progress bar for the temporary hit
 * points. Upon clicking the button, a dialog opens that allows the user to view and edit the hit points.
 *
 * @see HitPointsDialog
 */
public class HitPointsDisplay extends VerticalLayout {

    private final HitPoints hitPoints;

    /**
     * Creates a new display for the given hit points.
     *
     * @param hitPoints the hit points to display
     * @param characterSaver used to save the corresponding character when editing hit points in the dialog
     *         opened by clicking the button
     */
    public HitPointsDisplay(HitPoints hitPoints, CharacterSaver characterSaver) {
        this.hitPoints = hitPoints;

        setSpacing(false);
        setPadding(false);

        Button textButton = new Button(genHPString());
        hitPoints.getMaxHitPoints().addValueChangeListener(e -> textButton.setText(genHPString()));
        hitPoints.registerCurrentHPChangeListener(e -> textButton.setText(genHPString()));
        hitPoints.registerTempHPChangeListener(e -> textButton.setText(genHPString()));
        textButton.addClickListener(e -> new HitPointsDialog(hitPoints, characterSaver).open());
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        ProgressBar hitPointsBar = createHPBar();
        ProgressBar tempHPBar = createTempHPBar();

        add(textButton, hitPointsBar, tempHPBar);
    }

    private String genHPString() {
        String tempHPString = hitPoints.getTemporaryHitPoints() > 0 ? hitPoints.getTemporaryHitPoints() + " + " : "";
        return tempHPString + hitPoints.getCurrentHitPoints() + " / " + hitPoints.getMaxHitPoints().getValue();
    }

    private ProgressBar createHPBar() {
        ProgressBar hitPointsBar = new ProgressBar();
        hitPointsBar.setMin(0);
        Integer max = hitPoints.getMaxHitPoints().getValue();
        int hp = hitPoints.getCurrentHitPoints();
        hitPointsBar.setMax(max);
        hitPointsBar.setValue(hp);
        hitPointsBar.getElement().getStyle().set("margin", "5px");
        hitPointsBar.addThemeVariants(hp > max / 4 ? ProgressBarVariant.LUMO_SUCCESS : ProgressBarVariant.LUMO_ERROR);
        hitPoints.registerCurrentHPChangeListener(e -> {
            hitPointsBar.setValue(e.getNewValue());
            hitPointsBar.removeThemeVariants(ProgressBarVariant.LUMO_SUCCESS, ProgressBarVariant.LUMO_ERROR);
            hitPointsBar.addThemeVariants(
                    e.getNewValue() > hitPoints.getMaxHitPoints().getValue() / 4 ? ProgressBarVariant.LUMO_SUCCESS :
                            ProgressBarVariant.LUMO_ERROR);
        });
        hitPoints.getMaxHitPoints().addValueChangeListener(e -> hitPointsBar.setMax(e.getNewValue()));
        return hitPointsBar;
    }

    private ProgressBar createTempHPBar() {
        ProgressBar tempHPBar = new ProgressBar();
        tempHPBar.setMin(0);
        tempHPBar.setMax(hitPoints.getMaxHitPoints().getValue());
        tempHPBar.setValue(Math.min(hitPoints.getTemporaryHitPoints(), hitPoints.getMaxHitPoints().getValue()));
        tempHPBar.getElement().getStyle().set("margin", "5px");
        tempHPBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
        hitPoints.registerTempHPChangeListener(
                e -> tempHPBar.setValue(Math.min(e.getNewValue(), hitPoints.getMaxHitPoints().getValue())));
        hitPoints.getMaxHitPoints().addValueChangeListener(e -> tempHPBar.setMax(e.getNewValue()));
        return tempHPBar;
    }
}
