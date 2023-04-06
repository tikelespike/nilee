package com.tikelespike.nilee.app.components;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;

public class HitPointsDisplay extends VerticalLayout {

    private final HitPoints hitPoints;

    public HitPointsDisplay(PlayerCharacter pc, PlayerCharacterService characterService) {
        this.hitPoints = pc.getHitPoints();

        setSpacing(false);
        setPadding(false);

        Button textButton = new Button(genHPString());
        hitPoints.getMaxHitPoints().addValueChangeListener(e -> textButton.setText(genHPString()));
        hitPoints.registerCurrentHPChangeListener(e -> textButton.setText(genHPString()));
        textButton.addClickListener(e -> new HitPointsDialog(pc, characterService).open());
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
            hitPointsBar.addThemeVariants(e.getNewValue() > hitPoints.getMaxHitPoints().getValue() / 4 ? ProgressBarVariant.LUMO_SUCCESS : ProgressBarVariant.LUMO_ERROR);
        });
        hitPoints.getMaxHitPoints().addValueChangeListener(e -> {
            hitPointsBar.setMax(e.getNewValue());
        });
        return hitPointsBar;
    }

    private ProgressBar createTempHPBar() {
        ProgressBar tempHPBar = new ProgressBar();
        tempHPBar.setMin(0);
        tempHPBar.setMax(hitPoints.getMaxHitPoints().getValue());
        tempHPBar.setValue(Math.min(hitPoints.getTemporaryHitPoints(), hitPoints.getMaxHitPoints().getValue()));
        tempHPBar.getElement().getStyle().set("margin", "5px");
        tempHPBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
        hitPoints.registerTempHPChangeListener(e -> {
            tempHPBar.setValue(Math.min(e.getNewValue(), hitPoints.getMaxHitPoints().getValue()));
        });
        hitPoints.getMaxHitPoints().addValueChangeListener(e -> {
            tempHPBar.setMax(e.getNewValue());
        });
        return tempHPBar;
    }
}
