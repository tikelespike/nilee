package com.tikelespike.nilee.core.dice;

/**
 * The standard dice used in many tabletop role-playing games, including D&D 5e.
 */
public enum StandardDie {

    /**
     * A four-sided die. Is usually used to determine the damage of a spell or weapon in D&D 5e.
     */
    D4(4),

    /**
     * A six-sided die (most common outside of RPGs). Is usually used to determine the damage of a spell or weapon in
     * D&D 5e.
     */
    D6(6),

    /**
     * An eight-sided die. Is usually used to determine the damage of a spell or weapon in D&D 5e.
     */
    D8(8),

    /**
     * A ten-sided die. Is usually used to determine the damage of a spell or weapon in D&D 5e.
     */
    D10(10),

    /**
     * A twelve-sided die. Is usually used to determine the damage of a spell or weapon in D&D 5e.
     */
    D12(12),

    /**
     * A twenty-sided die. Is usually used to determine success or failure for an intended action with a set difficulty
     * in D&D 5e.
     */
    D20(20),

    /**
     * A one-hundred-sided die. Is usually used to determine a random number within an especially wide range or to make
     * percentage-based rolls in D&D 5e.
     */
    D100(100);

    private final int sides;

    StandardDie(int sides) {
        this.sides = sides;
    }

    /**
     * @return the number of sides of this die
     */
    public int getSides() {
        return sides;
    }
}
