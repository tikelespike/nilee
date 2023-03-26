package com.tikelespike.nilee.app.views.character;

import com.tikelespike.nilee.core.data.entity.character.stats.hitpoints.HitPoints;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class HitPointsDisplay extends Div {

        private final HitPoints hitPoints;

        public HitPointsDisplay(HitPoints hitPoints) {
            this.hitPoints = hitPoints;

            ProgressBar hitPointsBar = createHPBar();

            add(hitPointsBar);
        }

    private ProgressBar createHPBar() {
        ProgressBar hitPointsBar = new ProgressBar();
        hitPointsBar.setMin(0);
        hitPointsBar.setMax(hitPoints.getMaxHitPoints().getValue());
        hitPointsBar.setValue(hitPoints.getCurrentHitPoints());
        return hitPointsBar;
    }
}
