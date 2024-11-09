package com.tikelespike.nilee.core.character.stats;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;

/**
 * The proficiency bonus of a character is a representation of their general experience and skill. It grows when levels
 * are gained across classes. It is added to certain rolls, like saving throws and skill checks, when the character is
 * considered proficient in the task.
 *
 * @see com.tikelespike.nilee.core.character.stats.ability.ProficiencyLevel
 * @see <a href="https://www.dndbeyond.com/sources/dnd/basic-rules-2014/step-by-step-characters#ProficiencyBonus"
 *         >Proficiency Bonus on D&D Beyond</a>
 */
public class ProficiencyBonus extends Property<Integer> {

    /**
     * Creates a new proficiency bonus property with the default calculation method.
     *
     * @param totalCharacterLevel the total level of the character, accumulated across all classes
     */
    public ProficiencyBonus(Property<Integer> totalCharacterLevel) {
        super(new PropertyBaseSupplier<>() {
            @Override
            public Integer getBaseValue() {
                //CHECKSTYLE.OFF: MagicNumber
                return (totalCharacterLevel.getValue() - 1) / 4 + 2;
                //CHECKSTYLE.ON: MagicNumber
            }

            @Override
            public LocalizedString getAbstractDescription() {
                return t -> t.translate("core.character.ability.proficiencies.proficiency_bonus_default_calculation");
            }

            @Override
            public LocalizedString getSourceName() {
                return t -> t.translate("core.character.ability.proficiencies.proficiency_bonus_default_description");
            }
        });
        setBaseValueSelector(new MaxValueSelector<>());
    }
}
