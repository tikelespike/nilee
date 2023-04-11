package com.tikelespike.nilee.core.character.stats;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;

/**
 * A property describing the armor class of a character. The armor class is the value that is used to determine whether
 * an attack on the character hits or misses. Will always provide at least the {@link DefaultAC default armor class}
 * as a base value.
 */
public class ArmorClass extends Property<Integer> {

    /**
     * Creates a new armor class property based on the given dexterity property.
     *
     * @param dex the dexterity property of the character whose armor class this is referring to
     */
    public ArmorClass(AbilityScore dex) {
        // armor class is subject to maximization
        setBaseValueSelector(new MaxValueSelector<>());

        addBaseValueSupplier(new DefaultAC(dex));
    }
}
