package com.tikelespike.nilee.app.components;

import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class HitPointsDisplay extends VerticalLayout {

        private final HitPoints hitPoints;

        public HitPointsDisplay(HitPoints hitPoints) {
            this.hitPoints = hitPoints;

            Button textButton = new Button(hitPoints.getCurrentHitPoints() + "/" + hitPoints.getMaxHitPoints().getValue());
            setDefaultHorizontalComponentAlignment(Alignment.CENTER);

            ProgressBar hitPointsBar = createHPBar();
            ProgressBar tempHPBar = createTempHPBar();

            add(textButton, hitPointsBar, tempHPBar);
        }

    private ProgressBar createHPBar() {
        ProgressBar hitPointsBar = new ProgressBar();
        hitPointsBar.setMin(0);
        hitPointsBar.setMax(hitPoints.getMaxHitPoints().getValue());
        hitPointsBar.setValue(hitPoints.getCurrentHitPoints());
        return hitPointsBar;
    }

    private ProgressBar createTempHPBar() {
        ProgressBar tempHPBar = new ProgressBar();
        tempHPBar.setMin(0);
        tempHPBar.setMax(hitPoints.getMaxHitPoints().getValue());
        tempHPBar.setValue(Math.min(hitPoints.getCurrentHitPoints(), hitPoints.getMaxHitPoints().getValue()));
        return tempHPBar;
    }
}
