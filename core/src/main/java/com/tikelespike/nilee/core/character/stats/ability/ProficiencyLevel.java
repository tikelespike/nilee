package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.dice.DiceConstant;
import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.dice.DiceSum;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.PropertyModifier;

/**
 * Degree of proficiency in a skill or tool. In addition to just being proficient or not, a character can be half
 * proficient (for example, from the bard's Jack of All Trades feature) or have expertise in a skill (for example, from
 * the rogue's Expertise feature).
 */
public enum ProficiencyLevel {

    // Note: Ordering matters for natural ordering (Comparable interface)

    /**
     * Not proficient in the skill or tool. No bonus is applied.
     */
    NOT_PROFICIENT(0, t -> t.translate("core.character.ability.proficiencies.not_proficient")),

    /**
     * Half proficient in the skill or tool. The proficiency bonus is halved.
     */
    HALF_PROFICIENT(0.5, t -> t.translate("core.character.ability.proficiencies.half_proficient")),

    /**
     * Proficient in the skill or tool. The proficiency bonus is applied as normal.
     */
    PROFICIENT(1.0, t -> t.translate("core.character.ability.proficiencies.proficient")),

    /**
     * Expert in the skill or tool. The proficiency bonus is doubled.
     */
    EXPERTISE(2.0, t -> t.translate("core.character.ability.proficiencies.expertise"));

    private final double proficiencyBonusMultiplier;
    private final LocalizedString displayName;

    ProficiencyLevel(double proficiencyBonusMultiplier, LocalizedString displayName) {
        this.proficiencyBonusMultiplier = proficiencyBonusMultiplier;
        this.displayName = displayName;
    }

    /**
     * Applies the proficiency level to the given proficiency bonus to determine the effective bonus to apply to a
     * roll.
     *
     * @param proficiencyBonus the proficiency bonus of the character
     *
     * @return the effective bonus to apply to a roll based on the proficiency level
     */
    public PropertyModifier<DiceExpression> getBonusModifier(Property<Integer> proficiencyBonus) {
        return new PropertyModifier<>() {
            @Override
            public DiceExpression apply(DiceExpression value) {
                return new DiceSum(value,
                        new DiceConstant((int) (proficiencyBonusMultiplier * proficiencyBonus.getValue())));
            }

            @Override
            public LocalizedString getAbstractDescription() {
                return t -> {
                    String bonusString =
                            t.translate("core.character.ability.proficiencies.proficiency_bonus_abbreviation");
                    if (proficiencyBonusMultiplier != 1.0) {
                        bonusString = proficiencyBonusMultiplier + " " + t.translate(
                                "core.character.modifier.multiplicative" + ".operator", bonusString);
                    }
                    return t.translate("core.character.modifier.additive.operator", bonusString);
                };
            }

            @Override
            public LocalizedString getConcreteDescription() {
                return t -> t.translate("core.character.modifier.additive.operator", proficiencyBonusMultiplier);
            }

            @Override
            public LocalizedString getSourceName() {
                return displayName;
            }
        };
    }
}
