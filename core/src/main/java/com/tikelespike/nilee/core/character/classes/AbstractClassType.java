package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.character.stats.ability.ProficiencyLevel;

import java.util.EnumMap;
import java.util.Map;

/**
 * A convention-based abstract implementation of a CharacterClassArchetype which already implements some common logic
 * for convenience while leaving space for actual game design of the class. This class acts as an adapter between the
 * CharacterClassArchetype interface and an interface more convenient to use by content implementations.
 * <p>
 * Please note that the state of instances of this class should be immutable after construction.
 */
public abstract class AbstractClassType implements CharacterClassArchetype {
    private final Map<Ability, ProficiencyLevel> savingThrowProficiencies = new EnumMap<>(Ability.class);

    /**
     * Configures this archetype to grant player characters the specified level of proficiency in the specified saving
     * throw.
     *
     * @param ability the type of saving throw
     * @param proficiencyLevel how much proficiency this class grants a character in the specified saving
     *         throws
     */
    protected final void setSavingThrowProficiency(Ability ability, ProficiencyLevel proficiencyLevel) {
        savingThrowProficiencies.put(ability, proficiencyLevel);
    }

    /**
     * Configures this archetype to grant player characters proficiency in the specified saving throw.
     *
     * @param ability the type of saving throw
     */
    protected final void setSavingThrowProficiency(Ability ability) {
        savingThrowProficiencies.put(ability, ProficiencyLevel.PROFICIENT);
    }

    /**
     * Returns the saving throw proficiencies granted by this archetype.
     *
     * @return the saving throw proficiencies granted by this archetype
     */
    public final Map<Ability, ProficiencyLevel> getSavingThrowProficiencies() {
        return Map.copyOf(savingThrowProficiencies);
    }
}
