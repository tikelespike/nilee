package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.events.UpdateSubject;

/**
 * Provides a base value for a property, and a description of where the value comes from and how it is calculated. A
 * base value is one that computes a value from scratch, as opposed to a {@link PropertyModifier modifier} which derives
 * its value from another value.
 * <p>
 * An example of a base value for a property describing a character's armor class may be one that computes the standard
 * 10 + DEX value. Another one may compute the base AC for a specific armor, usually with a higher value.
 * <p>
 * If the value provided by the implementing class is not constant, it should notify its observers when the value that
 * would be returned on a call to {@link #getBaseValue()} changes. This is done by calling {@link #update()} when the
 * value changes. Alternatively, if the provided value depends on another {@link Property}, that property should be
 * added as a dependency using {@link #addDependency(UpdateSubject)} or by passing it to the constructor. This will
 * ensure the update method is called appropriately.
 *
 * @param <T> the type of the property value
 */
public abstract class PropertyBaseSupplier<T> extends UpdateSubject {

    /**
     * Initializes the base supplier with the given property dependencies. The supplier will notify its observers when
     * any of the dependencies change. If the supplier's supplied base value depends on any non-constant state, that
     * state should either be added to these dependencies if it is described by a property, or the implementing subclass
     * should call {@link #update()} when the value changes.
     * <p>
     * Dependencies can also be added later by calling {@link #addDependency(UpdateSubject)} )}.
     *
     * @param dependencies the properties this supplier depends on
     */
    protected PropertyBaseSupplier(Property<?>... dependencies) {
        super(dependencies);
    }

    /**
     * Returns a base value as described by the implementing class. This value may be constant or may change over time.
     * If the value changes, the implementing class should notify its observers by calling {@link #update()} on a
     * change. These events can then be observed by registered listeners (see
     * {@link #addUpdateListener(com.tikelespike.nilee.core.events.EventListener)}).
     *
     * @return the base value. May not be null.
     */
    public abstract T getBaseValue();

    /**
     * Returns an abstract "formula" of how the base value is computed in {@link #getBaseValue()}. This description
     * should be suitable for display in a user interface. For example, a base value that computes the standard 10 + DEX
     * value for a character's armor class may return "10 + DEX". Do not include any additional characters like
     * parentheses, brackets or colons.
     * <p>
     * The description returned should in most cases be constant. If the description does change, the implementing class
     * must notify its observers by calling {@link #update()} on a change, firing an
     * {@link com.tikelespike.nilee.core.property.events.UpdateEvent}. Alternatively, if the description depends on a
     * {@link Property}, that property should be added as a dependency using {@link #addDependency(UpdateSubject)}.
     *
     * @return a description of the base value computation (e.g. "10 + DEX")
     */
    public abstract LocalizedString getAbstractDescription();

    /**
     * Returns a short name describing the implementing class. This name should be suitable for display in a user
     * interface and provides information for the user on where the provided base value comes from. For example, a base
     * value that computes an armor's base AC may return "Leather Armor".
     * <p>
     * The description returned should be constant, that is, subsequent calls to this method should return the same
     * value. If the source name does change, the implementing class should notify its observers by calling
     * {@link #update()} on a change, firing an {@link com.tikelespike.nilee.core.property.events.UpdateEvent}.
     * Alternatively, if the source name depends on a {@link Property}, that property should be added as a dependency
     * using {@link #addDependency(UpdateSubject)}.
     *
     * @return a short name describing the implementing class (e.g. "Leather Armor")
     */
    public abstract LocalizedString getSourceName();
}
