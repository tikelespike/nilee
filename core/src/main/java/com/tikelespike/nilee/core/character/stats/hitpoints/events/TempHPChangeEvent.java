package com.tikelespike.nilee.core.character.stats.hitpoints.events;

import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.tikelespike.nilee.core.property.events.ValueChangeEvent;

/**
 * An event that is fired when the temporary hit points of a {@link HitPoints} object change.
 */
public class TempHPChangeEvent extends ValueChangeEvent<Integer> {

    /**
     * Creates a new {@link TempHPChangeEvent}. Old and new value may be equal.
     *
     * @param oldValue the value {@link HitPoints#getTemporaryHitPoints()} returned before the change causing
     *         this event
     * @param newValue the value {@link HitPoints#getTemporaryHitPoints()} returns after the change causing this
     *         event
     */
    public TempHPChangeEvent(Integer oldValue, Integer newValue) {
        super(oldValue, newValue);
    }
}
