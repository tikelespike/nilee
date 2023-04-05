package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.character.stats.hitpoints.events.CurrentHPChangeEvent;
import com.tikelespike.nilee.core.character.stats.hitpoints.events.TempHPChangeEvent;
import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.property.events.ValueChangeEvent;

public class HitPoints extends AbstractEntity {

    private final EventBus bus = new EventBus();
    private HitPointMax maxHitPoints;

    private int currentHitPoints;
    private int temporaryHitPoints;


    public HitPoints(AbilityScore constitution) {
        maxHitPoints = new HitPointMax(constitution);
        currentHitPoints = maxHitPoints.getValue();
        temporaryHitPoints = 5; // TESTING
        init();
    }

    public HitPointMax getMaxHitPoints() {
        return maxHitPoints;
    }

    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(int currentHitPoints) {
        int oldHP = this.currentHitPoints;
        this.currentHitPoints = currentHitPoints;
        bus.fireEvent(new CurrentHPChangeEvent(oldHP, currentHitPoints));
    }

    public int getTemporaryHitPoints() {
        return temporaryHitPoints;
    }

    public void setTemporaryHitPoints(int temporaryHitPoints) {
        int oldHP = this.temporaryHitPoints;
        this.temporaryHitPoints = temporaryHitPoints;
        bus.fireEvent(new TempHPChangeEvent(oldHP, temporaryHitPoints));
    }

    public void takeDamage(int damage) {
        int oldHP = currentHitPoints;
        int tempHPDiff = Math.min(damage, temporaryHitPoints);
        temporaryHitPoints -= tempHPDiff;
        currentHitPoints -= Math.max(damage - tempHPDiff, 0);
        bus.fireEvent(new TempHPChangeEvent(temporaryHitPoints + tempHPDiff, temporaryHitPoints));
        bus.fireEvent(new CurrentHPChangeEvent(oldHP, currentHitPoints));
    }

    public void heal(int healing) {
        int oldHP = currentHitPoints;
        currentHitPoints = Math.min(currentHitPoints + healing, maxHitPoints.getValue());
        bus.fireEvent(new CurrentHPChangeEvent(oldHP, currentHitPoints));
    }

    public Registration registerCurrentHPChangeListener(EventListener<CurrentHPChangeEvent> listener) {
        return bus.registerListener(CurrentHPChangeEvent.class, listener);
    }

    public Registration registerTempHPChangeListener(EventListener<TempHPChangeEvent> listener) {
        return bus.registerListener(TempHPChangeEvent.class, listener);
    }

    private void init() {
        maxHitPoints.addValueChangeListener(event -> {
            setCurrentHitPoints(Math.min(currentHitPoints, event.getNewValue()));
        });
    }
}
