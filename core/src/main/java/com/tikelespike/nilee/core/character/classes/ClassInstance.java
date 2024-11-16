package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.character.stats.ability.ProficiencyLevel;

import java.util.Map;

/**
 * Manages the D&D 5e class state different between multiple manifestations of the same {@link ClassArchetype} across
 * different player characters.
 * <p>
 * A class is the primary definition of what a player character can do. It specifies the character's main features in
 * combat, exploration, and interaction. A character can have different levels in a class, and will usually gain new
 * features as they level up.
 * <p>
 * Each instance of this class is linked to a ClassArchetype instance, which defines the "blueprint" and ruleset for the
 * way that class works, while this instance represents a specific incarnation of this class adapted to the character,
 * like the level achieved in this class and choices made while leveling up.
 *
 * @see <a href="https://www.dndbeyond.com/sources/dnd/basic-rules-2014/classes">Classes on D&D Beyond</a>
 */
public interface ClassInstance {

    // The generic definition of class archetypes and instances is still work in progress, for now classes are only
    // used as a source for saving throw proficiencies. This will be expanded in the future.

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

    /**
     * Returns the class archetype this instance is based on. The archetype defines the type of D&D 5e class (e.g.
     * fighter, wizard, rogue, ...) and the general ruleset for that D&D 5e class (while this instance represents its
     * dynamic, character-dependent state).
     *
     * @return the general type of class this instance models the dynamic state of
     */
    ClassArchetype<? extends ClassInstance> getArchetype();
}
