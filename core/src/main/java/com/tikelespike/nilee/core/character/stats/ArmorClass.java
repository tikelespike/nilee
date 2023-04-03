package com.tikelespike.nilee.core.character.stats;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.content.core.DefaultAC;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;
import com.tikelespike.nilee.core.property.Property;

/**
 * A property describing the armor class of a character. The armor class is the value that is used to determine whether
 * an attack on the character hits or misses. Will always provide at least the {@link DefaultAC default armor class}
 * as a base value.
 */
public class ArmorClass extends Property<Integer> {

    private Property<Integer> dex;

    /**
     * Default constructor for JPA. Do not use.
     */
    protected ArmorClass() {
        // armor class is subject to maximization
        setBaseValueSelector(new MaxValueSelector<>());
    }

    /**
     * Creates a new armor class property based on the given dexterity property.
     *
     * @param dex the dexterity property of the character whose armor class this is referring to
     */
    public ArmorClass(AbilityScore dex) {
        this();
        this.dex = dex;
        addBaseValueSupplier(new DefaultAC(dex));
    }
}