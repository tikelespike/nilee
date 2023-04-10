package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.character.stats.hitpoints.events.CurrentHPChangeEvent;
import com.tikelespike.nilee.core.character.stats.hitpoints.events.TempHPChangeEvent;
import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A character’s hit points define how tough that character is in combat and other dangerous situations.
 * This class manages all hit point related properties and methods, like the current hit points, temporary hit points, and
 * hit point maximum.
 *
 * @see <a href="https://www.dndbeyond.com/sources/basic-rules/combat#HitPoints">Hit Points on D&D Beyond</a>
 */
public class HitPoints extends AbstractEntity {

    private final EventBus bus = new EventBus();
    private final HitPointMax maxHitPoints;

    private int currentHitPoints;
    private int temporaryHitPoints;


    /**
     * Creates a new {@link HitPoints} object encapsulating hit points, temporary hit points, and the hit point maximum for
     * a character.
     *
     * @param constitution the constitution score of the character (its modifier is added to the hit point maximum)
     */
    public HitPoints(@NotNull AbilityScore constitution) {
        Objects.requireNonNull(constitution);
        maxHitPoints = new HitPointMax(constitution);
        currentHitPoints = maxHitPoints.getValue();
        init();
    }

    /**
     * Returns the hit point maximum property of this character.
     *
     * @return the hit point maximum of this character
     */
    public HitPointMax getMaxHitPoints() {
        return maxHitPoints;
    }

    /**
     * Returns the current hit points of this character.
     *
     * @return the current hit points of this character
     */
    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    /**
     * Sets the current hit points of this character. This method fires a {@link CurrentHPChangeEvent} if the new value
     * differs from the old value.
     *
     * @param currentHitPoints the new current hit points of this character, must be greater than or equal to 0 and should
     *                         not be greater than the hit point maximum
     * @throws IllegalArgumentException if the given value is less than 0
     */
    public void setCurrentHitPoints(int currentHitPoints) {
        if (currentHitPoints < 0) {
            throw new IllegalArgumentException("Current hit points must be greater than or equal to 0.");
        }
        int oldHP = this.currentHitPoints;
        this.currentHitPoints = currentHitPoints;
        if (oldHP != currentHitPoints) {
            bus.fireEvent(new CurrentHPChangeEvent(oldHP, currentHitPoints));
        }
    }

    /**
     * Returns the temporary hit points of this character.
     *
     * @return the temporary hit points of this character
     * @see <a href="https://www.dndbeyond.com/sources/basic-rules/combat#TemporaryHitPoints">Temporary Hit Points on D&D Beyond</a>
     */
    public int getTemporaryHitPoints() {
        return temporaryHitPoints;
    }

    /**
     * Sets the temporary hit points of this character. This method fires a {@link TempHPChangeEvent} if the new value
     * differs from the old value.
     *
     * @param temporaryHitPoints the new temporary hit points of this character, must be greater than or equal to 0
     * @throws IllegalArgumentException if the given value is less than 0
     * @see <a href="https://www.dndbeyond.com/sources/basic-rules/combat#TemporaryHitPoints">Temporary Hit Points on D&D Beyond</a>
     */
    public void setTemporaryHitPoints(int temporaryHitPoints) {
        if (temporaryHitPoints < 0) {
            throw new IllegalArgumentException("Temporary hit points must be greater than or equal to 0.");
        }
        int oldHP = this.temporaryHitPoints;
        this.temporaryHitPoints = temporaryHitPoints;
        if (oldHP != temporaryHitPoints) {
            bus.fireEvent(new TempHPChangeEvent(oldHP, temporaryHitPoints));
        }
    }

    /**
     * Takes the given amount of damage from this character. This method first reduces the temporary hit points of this
     * character, if they have any. If the temporary hit points are reduced to 0, the remaining damage is
     * subtracted from the current hit points. After the damage is taken, this method fires events if the current hit
     * points or temporary hit points have changed, respectively.
     *
     * @param damage the amount of damage to take, has to be greater than or equal to 0
     */
    public void takeDamage(int damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must be greater than or equal to 0.");
        }
        int oldHP = currentHitPoints;
        int damageTakenByTempHP = Math.min(damage, temporaryHitPoints);
        temporaryHitPoints -= damageTakenByTempHP;
        int remainingDamage = damage - damageTakenByTempHP;
        currentHitPoints = Math.max(currentHitPoints - remainingDamage, 0);
        if (damageTakenByTempHP > 0) {
            bus.fireEvent(new TempHPChangeEvent(temporaryHitPoints + damageTakenByTempHP, temporaryHitPoints));
        }
        if (currentHitPoints != oldHP) {
            bus.fireEvent(new CurrentHPChangeEvent(oldHP, currentHitPoints));
        }
    }

    /**
     * Heals this character by the given amount. This method fires a {@link CurrentHPChangeEvent} if the current hit
     * points are changed. The new current hit points are capped at the hit point maximum.
     *
     * @param healing the amount of healing to apply, has to be greater than or equal to 0
     * @see <a href="https://www.dndbeyond.com/sources/basic-rules/combat#Healing">Healing on D&D Beyond</a>
     */
    public void heal(int healing) {
        if (healing < 0) {
            throw new IllegalArgumentException("Healing must be greater than or equal to 0.");
        }
        int oldHP = currentHitPoints;
        currentHitPoints = Math.min(currentHitPoints + healing, maxHitPoints.getValue());
        if (currentHitPoints != oldHP) {
            bus.fireEvent(new CurrentHPChangeEvent(oldHP, currentHitPoints));
        }
    }

    /**
     * Registers a listener for {@link CurrentHPChangeEvent}s. The listener will be notified whenever the current hit
     * points of this hit point object change.
     *
     * @param listener the listener to register
     * @return a {@link Registration} object that can be used to unregister the listener
     */
    public Registration registerCurrentHPChangeListener(@NotNull EventListener<CurrentHPChangeEvent> listener) {
        return bus.registerListener(CurrentHPChangeEvent.class, listener);
    }

    /**
     * Registers a listener for {@link TempHPChangeEvent}s. The listener will be notified whenever the temporary hit
     * points of this hit point object change.
     *
     * @param listener the listener to register
     * @return a {@link Registration} object that can be used to unregister the listener
     */
    public Registration registerTempHPChangeListener(@NotNull EventListener<TempHPChangeEvent> listener) {
        return bus.registerListener(TempHPChangeEvent.class, listener);
    }

    private void init() {
        maxHitPoints.addValueChangeListener(event -> setCurrentHitPoints(Math.min(currentHitPoints, event.getNewValue())));
    }
}
