package com.tikelespike.nilee.core.character.stats.ability;

/**
 * Degree of proficiency in a skill or tool. In addition to just being proficient or not, a character can be half
 * proficient (for example, from the bard's Jack of All Trades feature) or have expertise in a skill (for example, from
 * the rogue's Expertise feature).
 */
public enum ProficiencyLevel {

    /**
     * Not proficient in the skill or tool. No bonus is applied.
     */
    NOT_PROFICIENT(0),

    /**
     * Half proficient in the skill or tool. The proficiency bonus is halved.
     */
    HALF_PROFICIENT(0.5),

    /**
     * Proficient in the skill or tool. The proficiency bonus is applied as normal.
     */
    PROFICIENT(1.0),

    /**
     * Expert in the skill or tool. The proficiency bonus is doubled.
     */
    EXPERTISE(2.0);

    private final double proficiencyBonusMultiplier;

    ProficiencyLevel(double proficiencyBonusMultiplier) {
        this.proficiencyBonusMultiplier = proficiencyBonusMultiplier;
    }

    /**
     * Applies the proficiency level to the given proficiency bonus to determine the effective bonus to apply to a
     * roll.
     *
     * @param proficiencyBonus the proficiency bonus of the character
     *
     * @return the effective bonus to apply to a roll based on the proficiency level
     */
    public int getEffectiveBonus(int proficiencyBonus) {
        return (int) Math.floor(proficiencyBonus * proficiencyBonusMultiplier);
    }
}
