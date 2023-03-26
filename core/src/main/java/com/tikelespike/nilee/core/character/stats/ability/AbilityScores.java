package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;

public class AbilityScores extends AbstractEntity {

    private static final int DEFAULT_VALUE = 10;


    private AbilityScore strength;
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
