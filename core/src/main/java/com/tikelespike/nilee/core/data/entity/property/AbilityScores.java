package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AbilityScores extends AbstractEntity {

    public static final int DEFAULT_STRENGTH = 10;

    @OneToOne(cascade = CascadeType.ALL)
    private AbilityScore strength;

    public AbilityScores() {
        this.strength = new AbilityScore(DEFAULT_STRENGTH, "Strength");
    }

    public AbilityScore getStrength() {
        return strength;
    }
}
