package com.tikelespike.nilee.app.components;

import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.progressbar.ProgressBarVariant;

public class HitPointsDisplay extends VerticalLayout {

        private final HitPoints hitPoints;

        public HitPointsDisplay(HitPoints hitPoints) {
            this.hitPoints = hitPoints;

            setSpacing(false);
            setPadding(false);

            String tempHPString = hitPoints.getTemporaryHitPoints() > 0 ? hitPoints.getTemporaryHitPoints() + " + " : "";
            Button textButton = new Button(tempHPString + hitPoints.getCurrentHitPoints() + " / " + hitPoints.getMaxHitPoints().getValue());
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);

            ProgressBar hitPointsBar = createHPBar();
            ProgressBar tempHPBar = createTempHPBar();

            add(textButton, hitPointsBar, tempHPBar);
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
        return hitPointsBar;
    }

    private ProgressBar createTempHPBar() {
        ProgressBar tempHPBar = new ProgressBar();
        tempHPBar.setMin(0);
        tempHPBar.setMax(hitPoints.getMaxHitPoints().getValue());
        tempHPBar.setValue(Math.min(hitPoints.getTemporaryHitPoints(), hitPoints.getMaxHitPoints().getValue()));
        tempHPBar.getElement().getStyle().set("margin", "5px");
        tempHPBar.addThemeVariants(ProgressBarVariant.LUMO_CONTRAST);
        return tempHPBar;
    }
}
