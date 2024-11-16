package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.i18n.LocalizedString;

/**
 * Represents a class archetype. This is a generic definition of a class, which can be used to create instances of the
 * class.
 * <p>
 * A class is the primary definition of what a player character can do. It specifies the character's main features in
 * combat, exploration, and interaction. A character can have different levels in a class, and will usually gain new
 * features as they level up.
 * <p>
 * A character class archetype specifies the "blueprint" and ruleset of a specific class, while a {@link ClassInstance}
 * instance represents a specific incarnation of this class containing state adapted to a single player character.
 * <p>
 * The state of these archetypes should be immutable after construction.
 *
 * @param <ClassType> the type of class instances this archetype creates for storing character-specific state
 */
public interface ClassArchetype<ClassType extends ClassInstance> {

    /**
     * Returns the display name of the class.
     *
     * @return the display name of the class
     */
    LocalizedString getName();

    /**
     * Creates a new incarnation of this class archetype for a player character. The returned instance can be used to
     * manage the state of the class for a specific character.
     *
     * @return a new character class instance of the class archetype
     */
    ClassType getNewInstance();
}
