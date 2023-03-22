package com.tikelespike.nilee.core.data.entity.character.stats.hitpoints;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.data.entity.character.stats.ability.AbilityScore;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PostLoad;

@Entity
public class HitPoints extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private HitPointMax maxHitPoints;

    private int currentHitPoints;
    private int temporaryHitPoints;

    protected HitPoints() {
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


    @PostLoad
    private void postLoad() {
        init();
    }

    private void init() {
        maxHitPoints.addValueChangeListener(event -> {
            if (currentHitPoints > event.getNewValue())
                setCurrentHitPoints(event.getNewValue());
        });
    }
}
