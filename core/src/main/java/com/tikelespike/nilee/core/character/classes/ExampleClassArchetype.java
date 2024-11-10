package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.i18n.LocalizedString;

/**
 * Example 5e class.
 */
public class ExampleClassArchetype extends AbstractClassType {

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
    public CharacterClass getNewInstance() {
        return new ExampleClass(this);
    }
}
