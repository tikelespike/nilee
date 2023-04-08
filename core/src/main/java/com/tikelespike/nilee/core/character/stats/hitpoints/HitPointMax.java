package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScoreBaseSupplier;
import com.tikelespike.nilee.core.property.OverridableProperty;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;

/**
 * The property describing the upper limit of hit points a character can have. By default, this value is calculated
 * based on the constitution score and the class hit die.
 */
public class HitPointMax extends OverridableProperty<Integer> {

    private Property<Integer> base;

    private AbilityScoreBaseSupplier abilityScoreBaseSupplier;

    private HPMaxBaseSupplier hpMaxBaseSupplier;

    /**
     * Default constructor for JPA. Do not use.
     */
    protected HitPointMax() {
        setBaseValueSelector(new MaxValueSelector<>());
    }

    /**
     * Creates a new hit point max property based on the given constitution score.
     *
     * @param constitution the CON modifier of this property is used to calculate the default base value of this property
     */
    public HitPointMax(AbilityScore constitution) {
        this();
        abilityScoreBaseSupplier = new AbilityScoreBaseSupplier(constitution);
        base = new Property<>(abilityScoreBaseSupplier);
        hpMaxBaseSupplier = new HPMaxBaseSupplier(base);
        addBaseValueSupplier(hpMaxBaseSupplier);
    }
}