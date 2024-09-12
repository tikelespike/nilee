package com.tikelespike.nilee.core.character.stats.ability;

public enum ProficiencyLevel {
    NOT_PROFICIENT(0),
    HALF_PROFICIENT(0.5),
    PROFICIENT(1.0),
    EXPERTISE(2.0);

    private final double proficiencyBonusMultiplier;

    ProficiencyLevel(double proficiencyBonusMultiplier) {
        this.proficiencyBonusMultiplier = proficiencyBonusMultiplier;
    }

    public int getEffectiveBonus(int proficiencyBonus) {
        return (int) Math.floor(proficiencyBonus * proficiencyBonusMultiplier);
    }
}
