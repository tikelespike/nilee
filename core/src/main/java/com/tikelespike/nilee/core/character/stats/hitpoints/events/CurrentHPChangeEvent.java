package com.tikelespike.nilee.core.character.stats.hitpoints.events;

import com.tikelespike.nilee.core.property.events.ValueChangeEvent;

/**
 * An event that is fired when the current hit points of a
 * {@link com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints} object change.
 */
public class CurrentHPChangeEvent extends ValueChangeEvent<Integer> {

    /**
     * Creates a new {@link CurrentHPChangeEvent}. Old and new value may be equal.
     *
     * @param oldValue the value {@link HitPoints#getCurrentHitPoints()} returned before the change causing this
     *         event
     * @param newValue the value {@link HitPoints#getCurrentHitPoints()} returns after the change causing this
     *         event
     */
    public CurrentHPChangeEvent(Integer oldValue, Integer newValue) {
        super(oldValue, newValue);
    }
}
