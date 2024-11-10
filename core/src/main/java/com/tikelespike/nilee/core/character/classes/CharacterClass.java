package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.character.stats.ability.ProficiencyLevel;

import java.util.Map;

/**
 * Manages the character class state different between multiple manifestations of the same
 * {@link CharacterClassArchetype} across different player characters.
 * <p>
 * A class is the primary definition of what a player character can do. It specifies the character's main features in
 * combat, exploration, and interaction. A character can have different levels in a class, and will usually gain new
 * features as they level up.
 * <p>
 * Each instance of this class is linked to a CharacterClassArchetype instance, which defines the "blueprint" and
 * ruleset for the way that class works, while this instance represents a specific incarnation of this class adapted to
 * the player, like the level achieved in this class and choices made while leveling up.
 *
 * @see <a href="https://www.dndbeyond.com/sources/dnd/basic-rules-2014/classes">Classes on D&D Beyond</a>
 */
public interface CharacterClass {

    // This class is currently WIP and more of a stub to provide a source for saving throw proficiencies.

    /**
     * Returns the level this class is leveled up to. The level determines which features are provided to the
     * character.
     *
     * @return the level of the class
     */
    int getLevel();

    /**
     * Sets the level this class is leveled up to. The level determines which features are provided to the character.
     *
     * @param level the new level of the class
     */
    void setLevel(int level);

    /**
     * A class provides a character with proficiency in some saving throws. This method returns the saving throw
     * proficiency levels provided by this class.
     *
     * @return the proficiency levels for saving throws provided by this class
     */
    Map<Ability, ProficiencyLevel> getSavingThrowProficiencies();
}
