package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.character.classes.ClassInstance;
import com.tikelespike.nilee.core.character.stats.ProficiencyBonus;
import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseValue;

import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Groups the specific six {@link AbilityScore}s used in the 5th edition of Dungeons & Dragons, namely Strength,
 * Dexterity, Constitution, Intelligence, Wisdom and Charisma, into one entity. These scores provide a quick description
 * of every creature's physical and mental characteristics.
 *
 * @see <a href="https://www.dndbeyond.com/sources/basic-rules/using-ability-scores#UsingAbilityScores">Ability
 *         Scores on D&D Beyond</a>
 */
public class AbilityScores extends AbstractEntity {

    private static final int DEFAULT_VALUE = 10;


    private final Map<Ability, AbilityScore> scores = new EnumMap<>(Ability.class);
    private final Map<Ability, ConstantBaseValue<ProficiencyLevel>> savingThrowProficiencies =
            new EnumMap<>(Ability.class);

    /**
     * Creates a new ability score group with default values for all six scores.
     *
     * @param proficiencyBonus the proficiency bonus of the character (used for saving throws if proficient)
     */
    public AbilityScores(ProficiencyBonus proficiencyBonus) {
        for (Ability ability : Ability.values()) {
            scores.put(ability, new AbilityScore(DEFAULT_VALUE, ability, proficiencyBonus));
        }
    }

    /**
     * Returns an unmodifiable collection of all ability scores (strength, dexterity, constitution, intelligence, wisdom
     * and charisma) grouped in this entity.
     *
     * @return an unmodifiable collection of all ability scores
     */
    public Collection<AbilityScore> getAll() {
        return scores.values();
    }

    /**
     * Returns the score of the given ability.
     *
     * @param ability the ability to get the score for
     *
     * @return the score quantifying the given ability
     */
    public AbilityScore get(Ability ability) {
        return scores.get(ability);
    }

    /**
     * Updates the basic saving throw proficiencies of all abilities based on the characters classes.
     *
     * @param classes the current classes of the character
     */
    public void updateClassBasedSavingThrowProficiencies(List<ClassInstance> classes) {
        for (Ability ability : Ability.values()) {
            ConstantBaseValue<ProficiencyLevel> oldSupplier = savingThrowProficiencies.get(ability);
            if (oldSupplier != null) {
                get(ability).getSavingThrowProficiency().removeBaseValueSupplier(oldSupplier);
                savingThrowProficiencies.remove(ability);
            }
        }
        if (classes.isEmpty()) {
            return;
        }
        ClassInstance firstClass = classes.getFirst();
        for (Ability ability : firstClass.getSavingThrowProficiencies().keySet()) {
            ProficiencyLevel proficiency = firstClass.getSavingThrowProficiencies().get(ability);
            ConstantBaseValue<ProficiencyLevel> supplier =
                    new ConstantBaseValue<>(proficiency, firstClass.getArchetype().getName());
            get(ability).getSavingThrowProficiency().addBaseValueSupplier(supplier);
            savingThrowProficiencies.put(ability, supplier);
        }
    }
}
