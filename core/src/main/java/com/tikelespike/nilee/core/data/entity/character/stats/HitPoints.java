package com.tikelespike.nilee.core.data.entity.character.stats;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HitPoints extends AbstractEntity {
    @OneToOne
    private HitPointMax maxHitPoints;

    private int currentHitPoints;
    private int temporaryHitPoints;

    protected HitPoints() {
        init();
    }

    public HitPoints(AbilityScore constitution) {
        maxHitPoints = new HitPointMax(constitution);
        currentHitPoints = maxHitPoints.getValue();
        temporaryHitPoints = 0;
        init();
    }

    public HitPointMax getMaxHitPoints() {
        return maxHitPoints;
    }

    private void setMaxHitPoints(HitPointMax maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(int currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
    }

    public int getTemporaryHitPoints() {
        return temporaryHitPoints;
    }

    public void setTemporaryHitPoints(int temporaryHitPoints) {
        this.temporaryHitPoints = temporaryHitPoints;
    }

    public void takeDamage(int damage) {
        int tempHPDiff = Math.min(damage, temporaryHitPoints);
        temporaryHitPoints -= tempHPDiff;
        currentHitPoints -= Math.max(damage - tempHPDiff, 0);
    }

    public void heal(int healing) {
        currentHitPoints = Math.min(currentHitPoints + healing, maxHitPoints.getValue());
    }

    public void reset() {
        currentHitPoints = maxHitPoints.getValue();
        temporaryHitPoints = 0;
    }

    private void init() {
        maxHitPoints.addValueChangeListener(event -> {
            if (currentHitPoints > event.getNewValue())
                setCurrentHitPoints(event.getNewValue());
        });
    }
}
