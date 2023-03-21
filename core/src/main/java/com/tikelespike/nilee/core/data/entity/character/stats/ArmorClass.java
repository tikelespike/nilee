package com.tikelespike.nilee.core.data.entity.character.stats;

import com.tikelespike.nilee.core.content.core.DefaultAC;
import com.tikelespike.nilee.core.data.entity.property.convenience.MaxValueSelector;
import com.tikelespike.nilee.core.data.entity.property.Property;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 * A property describing the armor class of a character. The armor class is the value that is used to determine whether
 * an attack on the character hits or misses. Will always provide at least the {@link DefaultAC default armor class}
 * as a base value.
 */
@Entity
public class ArmorClass extends Property<Integer> {

    @OneToOne
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

    private void setDex(Property<Integer> dex) {
        this.dex = dex;
    }
    private Property<Integer> getDex() {
        return dex;
    }
}
