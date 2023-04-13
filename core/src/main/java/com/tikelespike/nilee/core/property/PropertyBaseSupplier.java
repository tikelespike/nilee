package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.events.UpdateEvent;
import com.tikelespike.nilee.core.property.events.UpdateSubject;

/**
 * Provides a base value for a property, and a description of where the value comes from and how it is calculated.
 * A base value is one that computes a value from scratch, as opposed to a {@link PropertyModifier modifier} which
 * derives its value from another value.
 * <p>
 * An example of a base value for a property describing a character's armor class may be one that computes the standard
 * 10 + DEX value. Another one may compute the base AC for an armor, which will give a higher value.
 * <p>
 * If the value provided by the implementing class is not constant, it should notify its observers when the value
 * that would be returned on a call to {@link #getBaseValue()} changes. This is done by using an {@link EventBus},
 * overriding the {@link UpdateSubject#addUpdateListener(EventListener)} method to register listeners to the bus,
 * and firing an {@link UpdateEvent} when the value changes.
 *
 * @param <T> the type of the property value
 */
public abstract class PropertyBaseSupplier<T> extends UpdateSubject {

    /**
     * Returns a base value as described by the implementing class. This value may be constant or may change over time.
     * If the value changes, the implementing class should notify its observers by overriding the
     * {@link UpdateSubject#addUpdateListener(EventListener)} method to register listeners to a bus, and
     * firing an {@link UpdateEvent} on a change.
     *
     * @return the base value. May not be null.
     */
    public abstract T getBaseValue();

    /**
     * Returns an abstract description of how the base value is computed in {@link #getBaseValue()}. This description
     * should be suitable for display in a user interface. For example, a base value that computes the standard 10 + DEX
     * value for a character's armor class may return "10 + DEX". Do not include any additional characters like
     * parentheses, brackets or colons.
     * <p>
     * The description returned should be constant. If the description changes, the implementing class should notify
     * its observers by overriding the {@link UpdateSubject#addUpdateListener(EventListener)} method to register
     * listeners to a bus, and firing an {@link UpdateEvent} on a change.
     *
     * @return a description of the base value computation (e.g. "10 + DEX")
     */
    public abstract LocalizedString getAbstractDescription();

    /**
     * Returns a short name describing the implementing class. This name should be suitable for display in a user
     * interface and provides information for the user on where the provided base value comes from.
     * For example, a base value that computes an armor's base AC may return "Leather Armor".
     * <p>
     * The description returned must be constant, that is, subsequent calls to this method must return the same value.
     *
     * @return a short name describing the implementing class (e.g. "Leather Armor")
     */
    public abstract LocalizedString getSourceName();
}
