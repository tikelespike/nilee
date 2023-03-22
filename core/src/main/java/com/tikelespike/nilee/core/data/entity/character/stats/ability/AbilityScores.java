package com.tikelespike.nilee.core.data.entity.character.stats.ability;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

@Entity
public class AbilityScores extends AbstractEntity {

    @Transient
    private static final int DEFAULT_VALUE = 10;


    @OneToOne(cascade = CascadeType.ALL)
    private AbilityScore strength;

    @OneToOne(cascade = CascadeType.ALL)
    private AbilityScore constitution;

    public AbilityScores() {
        this.strength = new AbilityScore(DEFAULT_VALUE, "Strength", "STR");
        this.constitution = new AbilityScore(DEFAULT_VALUE, "Constitution", "CON");
    }

    public AbilityScore getStrength() {
        return strength;
    }

    public AbilityScore getConstitution() {
        return constitution;
    }
}
