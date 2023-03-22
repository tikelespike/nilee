package com.tikelespike.nilee.core.data.entity.character.stats.hitpoints;

import com.tikelespike.nilee.core.data.entity.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.data.entity.character.stats.ability.AbilityScoreBaseSupplier;
import com.tikelespike.nilee.core.data.entity.property.convenience.MaxValueSelector;
import com.tikelespike.nilee.core.data.entity.property.Property;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HitPointMax extends Property<Integer> {

    @OneToOne(cascade = CascadeType.ALL)
    private Property<Integer> base;

    /**
     * Default constructor for JPA. Do not use.
     */
    protected HitPointMax() {
        setBaseValueSelector(new MaxValueSelector<>());
    }

    public HitPointMax(AbilityScore constitution) {
        this();
        base = new Property<>(new AbilityScoreBaseSupplier(constitution));
        addBaseValueSupplier(new HPMaxBaseSupplier(base));
    }

    public Property<Integer> getBaseValueProperty() {
        return base;
    }



    // JPA setters and getters

    private void setBase(Property<Integer> base) {
        this.base = base;
    }

    private Property<Integer> getBase() {
        return base;
    }
}