package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScoreBaseSupplier;
import com.tikelespike.nilee.core.property.OverridableProperty;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * The property describing the upper limit of hit points a character can have. By default, this value is calculated
 * based on the constitution score and the class hit die.
 */
public class HitPointMax extends OverridableProperty<Integer> {

    /**
     * Creates a new hit point max property based on the given constitution score.
     *
     * @param constitution the CON modifier of this property is used to calculate the default base value of this
     *         property
     */
    public HitPointMax(@NotNull AbilityScore constitution) {
        Objects.requireNonNull(constitution);
        setBaseValueSelector(new MaxValueSelector<>());
        AbilityScoreBaseSupplier abilityScoreBaseSupplier = new AbilityScoreBaseSupplier(constitution);
        Property<Integer> base = new Property<>(abilityScoreBaseSupplier);
        HPMaxBaseSupplier hpMaxBaseSupplier = new HPMaxBaseSupplier(base);
        addBaseValueSupplier(hpMaxBaseSupplier);
    }
}
