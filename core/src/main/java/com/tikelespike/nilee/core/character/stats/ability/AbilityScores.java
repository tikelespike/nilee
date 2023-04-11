package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.data.entity.AbstractEntity;

/**
 * Groups the specific six {@link AbilityScore}s used in the 5th edition of Dungeons & Dragons, namely
 * Strength, Dexterity, Constitution, Intelligence, Wisdom and Charisma, into one entity.
 * These scores provide a quick description of every creature's physical and mental characteristics.
 *
 * @see <a href="https://www.dndbeyond.com/sources/basic-rules/using-ability-scores#UsingAbilityScores">Ability Scores on D&D Beyond</a>
 */
public class AbilityScores extends AbstractEntity {

    private static final int DEFAULT_VALUE = 10;


    private final AbilityScore strength;
    private final AbilityScore constitution;

    /**
     * Creates a new ability score group with default values for all six scores.
     */
    public AbilityScores() {
        this.strength = new AbilityScore(DEFAULT_VALUE, "Strength", "STR");
        this.constitution = new AbilityScore(DEFAULT_VALUE, "Constitution", "CON");
    }

    /**
     * Returns the strength score of this ability score group.
     * Strength measures bodily power, athletic training, and the extent to which a creature can exert raw physical force.
     *
     * @return the strength score of this ability score group
     */
    public AbilityScore getStrength() {
        return strength;
    }

    /**
     * Returns the constitution score of this ability score group.
     * Constitution measures health, stamina, and vital force.
     *
     * @return the constitution score of this ability score group
     */
    public AbilityScore getConstitution() {
        return constitution;
    }
}
