package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.character.stats.ability.ProficiencyLevel;

import java.util.Map;

/**
 * A convention-based abstract implementation of a CharacterClass which already implements some common logic for
 * convenience while leaving space for actual game design of the class. This class acts as an adapter between the
 * CharacterClass interface and an interface more convenient to use by content implementations.
 */
public abstract class AbstractClass implements CharacterClass {

    private int level = 1;
    private final AbstractClassType classType;

    /**
     * Creates a new class instance.
     *
     * @param classType the archetype of this class specifying its rules and abilities that are not
     *         character-specific
     */
    protected AbstractClass(AbstractClassType classType) {
        this.classType = classType;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public final Map<Ability, ProficiencyLevel> getSavingThrowProficiencies() {
        return classType.getSavingThrowProficiencies();
    }

    @Override
    public CharacterClassArchetype getArchetype() {
        return classType;
    }
}
