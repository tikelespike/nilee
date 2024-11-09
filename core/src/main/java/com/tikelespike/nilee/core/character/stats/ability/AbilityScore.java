package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.PropertyModifier;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseProperty;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseValue;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;
import jakarta.validation.constraints.NotNull;

/**
 * One of the six {@link AbilityScores} (namely strength, dexterity, constitution, intelligence, wisdom, and charisma)
 * that provide a quick description of every creature's physical and mental characteristics. The three main rolls of the
 * game (attack, saving throw, and skill check) are all based on an ability score. An ability score can range from 1 to
 * 30, where 10 describes normal human average, and 18 is the highest that a person usually achieves. Player characters
 * can have a score of up to 20 (sometimes even higher), while some legendary monsters and devine beings can have scores
 * of up to 30.
 * <p>
 * Each ability score has a modifier (calculated as {@code (score - 10) / 2}) that is used in many calculations.
 *
 * @see <a href="https://www.dndbeyond.com/sources/basic-rules/using-ability-scores#UsingAbilityScores">Ability
 *         Scores on D&D Beyond</a>
 */
public class AbilityScore extends ConstantBaseProperty<Integer> {

    private static final int NEUTRAL_SCORE_VALUE = 10;

    private final Ability ability;

    private final Property<DiceExpression> checkRoll = new Property<>(new DefaultAbilityRoll(this));
    private final Property<ProficiencyLevel> savingThrowProficiency = new Property<>(
            new ConstantBaseValue<>(ProficiencyLevel.NOT_PROFICIENT,
                    t -> t.translate("core.character.ability.default_proficiency_description")),
            new MaxValueSelector<>());
    private final Property<DiceExpression> savingThrow = new Property<>(new DefaultAbilityRoll(this));

    private PropertyModifier<DiceExpression> savingThrowBonus;

    /**
     * Creates a new score for the given ability with the given default base value.
     *
     * @param defaultBase the initial, unmodified value of this ability score (usually rolled or assigned using
     *         a point-buy system upon character creation)
     * @param ability the ability type this score quantifies
     * @param proficiencyBonus the proficiency bonus of the character (used for saving throws if proficient).
     */
    public AbilityScore(int defaultBase, @NotNull Ability ability, @NotNull Property<Integer> proficiencyBonus) {
        super(defaultBase, ability.getLongName());
        this.ability = ability;
        savingThrowProficiency.addValueChangeListener(e -> {
            if (savingThrowBonus != null) {
                savingThrow.removeModifier(savingThrowBonus);
                savingThrowBonus = null;
            }
            if (e.getNewValue() != ProficiencyLevel.NOT_PROFICIENT) {
                savingThrowBonus = e.getNewValue().getBonusModifier(proficiencyBonus);
                savingThrow.addModifier(savingThrowBonus);
            }
        });
    }

    /**
     * Returns the property describing the dice expression used to calculate the result of a check roll using this
     * ability score. (For example, a check roll using the "Strength" ability score would be "{@code 1d20 + STR}" by
     * default.)
     *
     * @return the property describing the dice expression used to calculate the result of a check roll using this
     *         ability score
     */
    public Property<DiceExpression> getCheckRoll() {
        return checkRoll;
    }

    /**
     * Calculates the modifier of this ability score, which is equal to {@code (score value - 10) / 2}. For example, a
     * score of 8 or 9 has a modifier of -1, 10 or 11 of 0, 12 or 13 of 1, and so on.
     *
     * @return the modifier of this ability score
     */
    public int getModifier() {
        return Math.floorDiv(getValue() - NEUTRAL_SCORE_VALUE, 2);
    }

    /**
     * Returns the ability type this score quantifies.
     *
     * @return the ability type this score quantifies
     */
    public Ability getAbility() {
        return ability;
    }

    /**
     * Returns the property describing the proficiency level present in this ability score for saving throws. Can be
     * modified to reflect the proficiency level of the character.
     *
     * @return the property describing the proficiency level present in this ability score for saving throws
     */
    public Property<ProficiencyLevel> getSavingThrowProficiency() {
        return savingThrowProficiency;
    }

    /**
     * Returns the property describing the dice expression used to calculate the result of a saving throw using this
     * ability score.
     *
     * @return the property describing the dice expression used to calculate the result of a saving throw using this
     *         ability score
     */
    public Property<DiceExpression> getSavingThrow() {
        return savingThrow;
    }
}
