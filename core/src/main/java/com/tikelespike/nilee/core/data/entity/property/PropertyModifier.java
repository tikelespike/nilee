package com.tikelespike.nilee.core.data.entity.property;

/**
 * Modifies the value of a {@link Property} by applying a transformation to it.
 * <p>
 * For example, a modifier could add a bonus to a property's value, or subtract a penalty. Specifically, a Shield
 * could add a +2 bonus to the character's AC. A spell might double the movement speed of a character. A critical hit
 * might double the damage dice for a weapon, and so on.
 *
 * @param <T> the type of the value to modify
 */
public interface PropertyModifier<T> {

    /**
     * Applies a transformation to the given value and returns the result, which may not be null, but may be the same
     * as the input value.
     *
     * @param value the value to modify. Not null.
     * @return the modified value. May not be null. May be the same as the input value.
     */
    T apply(T value);

    /**
     * An abstract description of the transformation applied by the implementing class. This description should be
     * suitable for display in a user interface. This differs from the description returned in
     * {@link #getConcreteDescription()} in that it may contain variable names in place of their actual values.
     * <p>
     * For example, a modifier that adds a bonus to a property's value based
     * on a character's dex modifier may return "+ DEX".
     * The description may be the same as the one returned in {@link #getConcreteDescription()} if the modifier does
     * not use any game-level variables (character stats etc.).
     *
     * @return a description of the transformation applied by the implementing class which may contain variable names
     * (e.g. "+ DEX")
     */
    String getAbstractDescription();

    /**
     * A concrete description of the transformation applied by the implementing class. This description should be
     * suitable for display in a user interface. It differs from the description returned in
     * {@link #getAbstractDescription()}
     * in that it no longer contains any variable names, but instead all computed values used in the transformation.
     * <p>
     * For example, a modifier that adds a bonus to a property's value based
     * on a character's dex modifier may return "+ 2" if the character's dex modifier is 2.
     *
     * @return a description of the transformation applied by the implementing class that does not contain variable
     * names (e.g. "+ 2")
     */
    String getConcreteDescription();

    /**
     * Returns a short name describing the semantics of the implementing class.
     * This name should be suitable for display in a user
     * interface and provides information for the user on where the provided modification comes from.
     * For example, a bonus to AC given by the Shield spell may return "Shield (Spell)" to distinguish it from
     * a bonus given by a Shield item.
     *
     * @return a short name describing the implementing class (e.g. "Leather Armor")
     */
    String getSourceName();
}