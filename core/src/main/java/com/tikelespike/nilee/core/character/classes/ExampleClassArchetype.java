package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.i18n.LocalizedString;

/**
 * Example 5e class with dexterity and wisdom saving throw proficiencies.
 */
public class ExampleClassArchetype extends AbstractClassArchetype<ExampleClassInstance> {

    /**
     * Creates a new example class archetype.
     */
    public ExampleClassArchetype() {
        setSavingThrowProficiency(Ability.DEXTERITY);
        setSavingThrowProficiency(Ability.WISDOM);
    }

    @Override
    public LocalizedString getName() {
        return t -> "Test Class";
    }

    @Override
    public ExampleClassInstance getNewInstance() {
        return new ExampleClassInstance(this);
    }
}
