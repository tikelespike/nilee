package com.tikelespike.nilee.core.data.entity.character.stats.ability;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AbilityScores extends AbstractEntity {

    @OneToOne(cascade = CascadeType.ALL)
    private AbilityScore strength;

    public AbilityScores() {
        this.strength = AbilityScore.strength();
    }

    public AbilityScore getStrength() {
        return strength;
    }
}
