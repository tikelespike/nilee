package com.tikelespike.nilee.core.character.classes;

import com.tikelespike.nilee.core.character.stats.ability.Ability;
import com.tikelespike.nilee.core.i18n.LocalizedString;

/**
 * Example 5e class.
 */
public class OtherExampleClassArchetype extends AbstractClassType {

    /**
     * Creates a new example class archetype.
     */
    public OtherExampleClassArchetype() {
        setSavingThrowProficiency(Ability.CONSTITUTION);
    }

    @Override
    public LocalizedString getName() {
        return t -> "Other test class (con)";
    }

    @Override
    public CharacterClass getNewInstance() {
        return new OtherExampleClass(this);
    }
}
