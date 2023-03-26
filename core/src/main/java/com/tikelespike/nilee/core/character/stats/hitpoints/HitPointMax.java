package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScoreBaseSupplier;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;

public class HitPointMax extends Property<Integer> {

    private Property<Integer> base;

    private AbilityScoreBaseSupplier abilityScoreBaseSupplier;

    private HPMaxBaseSupplier hpMaxBaseSupplier;

    /**
     * Default constructor for JPA. Do not use.
     */
    protected HitPointMax() {
        setBaseValueSelector(new MaxValueSelector<>());
    }

    public HitPointMax(AbilityScore constitution) {
        this();
        abilityScoreBaseSupplier = new AbilityScoreBaseSupplier(constitution);
        base = new Property<>(abilityScoreBaseSupplier);
        hpMaxBaseSupplier = new HPMaxBaseSupplier(base);
        addBaseValueSupplier(hpMaxBaseSupplier);
    }

    public Property<Integer> getBaseValueProperty() {
        return base;
    }
}