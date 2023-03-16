package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.property.events.UpdateSubject;

/**
 * Provides a base value for a property, and a description of where the value comes from and how it is calculated.
 * A base value is one that computes a value from scratch, as opposed to a {@link PropertyModifier modifier} which
 * derives its value from another value.
 * <p>
 * An example of a base value for a property describing a character's armor class may be one that computes the standard
 * 10 + DEX value. Another one may compute the base AC for an armor, which will give a higher value.
 *
 * @param <T> the type of the property value
 */
public interface PropertyBaseSupplier<T> extends UpdateSubject {

    /**
     * Returns a base value as described by the implementing class.
     *
     * @return the base value. May not be null.
     */
    T getBaseValue();

    /**
     * Returns an abstract description of how the base value is computed in {@link #getBaseValue()}. This description
     * should be suitable for display in a user interface. For example, a base value that computes the standard 10 + DEX
     * value for a character's armor class may return "10 + DEX". Do not include any additional characters like
     * parentheses, brackets or colons.
     *
     * @return a description of the base value computation (e.g. "10 + DEX")
     */
    String getAbstractDescription();

    /**
     * Returns a short name describing the implementing class. This name should be suitable for display in a user
     * interface and provides information for the user on where the provided base value comes from.
     * For example, a base value that computes an armor's base AC may return "Leather Armor".
     *
     * @return a short name describing the implementing class (e.g. "Leather Armor")
     */
    String getSourceName();
}
