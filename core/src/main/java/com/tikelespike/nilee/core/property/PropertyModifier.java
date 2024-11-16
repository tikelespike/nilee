package com.tikelespike.nilee.core.property;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.events.UpdateSubject;

/**
 * Modifies the value of a {@link Property} by applying a transformation to it.
 * <p>
 * For example, a modifier could add a bonus to a property's value, or subtract a penalty. Specifically, a shield could
 * add a +2 bonus to the character's armor class. A spell might double the movement speed of a character. A critical hit
 * might double the damage dice for a weapon, and so on.
 * <p>
 * If the result returned from the modification depends on anything other than the input value, it should notify its
 * observers when the value that would be returned on a call to {@link #apply(Object)} changes for any given input. This
 * is done by calling {@link #update()} upon a change. Alternatively, if the result depends on a {@link Property}, that
 * property should be added as a dependency using {@link #addDependency(UpdateSubject)} or by passing it to the
 * constructor. This will ensure the update method is called appropriately.
 *
 * @param <T> the type of the value to modify
 */
public abstract class PropertyModifier<T> extends UpdateSubject {

    /**
     * Initializes the modifier with the given property dependencies. The modifier will notify its observers when any of
     * the dependencies change. If the modifier's transformation depends on any non-constant state, that state should
     * either be added to these dependencies if it is described by a property, or the implementing subclass should call
     * {@link #update()} when the value changes.
     *
     * @param dependencies the properties this modifier depends on
     */
    protected PropertyModifier(Property<?>... dependencies) {
        super(dependencies);
    }

    /**
     * Applies a transformation to the given value and returns the result, which may not be null, but may be the same as
     * the input value.
     * <p>
     * If the result returned from the modification depends on anything else than the input value, it should notify its
     * observers when the value that would be returned changes for any given input. This is done by calling
     * {@link #update()} when the value changes. Alternatively, if the result depends on the value of a
     * {@link Property}, that property should be added as a dependency using {@link #addDependency(UpdateSubject)}.
     *
     * @param value the value to modify. Not null.
     *
     * @return the modified value. May not be null. May be the same as the input value.
     */
    public abstract T apply(T value);

    /**
     * An abstract "formula" of the transformation applied by the implementing class. This description should be
     * suitable for display in a user interface. This differs from the description returned in
     * {@link #getConcreteDescription()} in that it may contain variable names in place of their actual current values.
     * <p>
     * For example, a modifier that adds a bonus to a property's value based on a character's dex modifier may return "+
     * DEX". The description may be the same as the one returned in {@link #getConcreteDescription()} if the modifier
     * does not use any game-level variables (character stats etc.).
     * <p>
     * The description returned should usually be constant. If the description does change, the implementing class
     * should notify its observers by calling {@link #update()} on a change. Alternatively, if the description depends
     * on a {@link Property}, that property should be added as a dependency using {@link #addDependency(UpdateSubject)}.
     * This will ensure the update method is called appropriately.
     *
     * @return a description of the transformation applied by the implementing class which may contain variable names
     *         (e.g. "+ DEX")
     */
    public abstract LocalizedString getAbstractDescription();

    /**
     * A concrete expression of the transformation applied by the implementing class. This description should be
     * suitable for display in a user interface. It differs from the description returned in
     * {@link #getAbstractDescription()} in that it no longer contains any variable names, but instead all computed
     * values used in the transformation.
     * <p>
     * For example, a modifier that adds a bonus to a property's value based on a character's dex modifier may return "+
     * 2" if the character's dex modifier is 2 at the time of calling.
     * <p>
     * If the result for a given input to {@link #apply(Object)} changes (for example, because the dex modifier of a
     * character changes), the description returned should change with it. If the description changes, the implementing
     * class should notify its observers by calling {@link #update()} on a change. Alternatively, if the description
     * depends on a {@link Property}, that property should be added as a dependency using
     * {@link #addDependency(UpdateSubject)}. This will ensure the update method is called appropriately.
     *
     * @return a description of the transformation applied by the implementing class that does not contain variable
     *         names (e.g. "+ 2")
     */
    public abstract LocalizedString getConcreteDescription();

    /**
     * Returns a short name describing the semantics of the implementing class. This name should be suitable for display
     * in a user interface and provides information for the user on where the provided modification comes from. For
     * example, a bonus to AC given by the Shield spell may return "Shield (Spell)" to distinguish it from a bonus given
     * by a shield item.
     * <p>
     * The name returned should be constant, that is, subsequent calls to this method should return the same value. If
     * the name does change, the implementing class should notify its observers by calling {@link #update()} on a
     * change. Alternatively, if the name depends on a {@link Property}, that property should be added as a dependency
     * using {@link #addDependency(UpdateSubject)}. This will ensure the update method is called appropriately.
     *
     * @return a short name describing the implementing class (e.g. "Leather Armor", "Shield (Spell)")
     */
    public abstract LocalizedString getSourceName();
}
