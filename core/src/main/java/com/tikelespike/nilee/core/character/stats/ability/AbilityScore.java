package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.convenience.ConstantBaseProperty;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * One of the six {@link AbilityScores} (namely strength, dexterity, constitution, intelligence, wisdom,
 * and charisma) that provide a quick description of every creature's physical and mental characteristics.
 * The three main rolls of the game (attack, saving throw, and skill check) are all based on an ability score.
 * An ability score can range from 1 to 30, where 10 describes normal human average, and 18 is the highest that a person
 * usually achieves. Player characters can have a score of up to 20 (sometimes even higher), while some legendary
 * monsters and devine beings can have scores of up to 30.
 * <p>
 * Each ability score has a modifier (calculated as {@code (score - 10) / 2}) that is used in many calculations.
 *
 * @see <a href="https://www.dndbeyond.com/sources/basic-rules/using-ability-scores#UsingAbilityScores">Ability Scores on D&D Beyond</a>
 */
public class AbilityScore extends ConstantBaseProperty {

    private final LocalizedString longName;
    private final LocalizedString shortName;

    private final Property<DiceExpression> checkRoll = new Property<>(new DefaultAbilityCheckRoll(this));

    /**
     * Creates a new ability score with the given default base value, long name, and short name.
     *
     * @param defaultBase the initial, unmodified value of this ability score (usually rolled or assigned using a point-buy system upon character creation)
     * @param longName    the name of this ability score (e.g. "Strength")
     * @param shortName   the abbreviated name of this ability score (e.g. "STR", usually referring to the modifier)
     */
    public AbilityScore(int defaultBase, @NotNull LocalizedString longName, @NotNull LocalizedString shortName) {
        super(defaultBase, longName);
        Objects.requireNonNull(longName);
        Objects.requireNonNull(shortName);
        this.longName = longName;
        this.shortName = shortName;
    }

    /**
     * Returns the property describing the dice expression used to calculate the result of a check roll using this
     * ability score. (For example, a check roll using the "Strength" ability score would be "{@code 1d20 + STR}"
     * by default.)
     *
     * @return the property describing the dice expression used to calculate the result of a check roll using this ability score
     */
    public Property<DiceExpression> getCheckRoll() {
        return checkRoll;
    }

    /**
     * Calculates the modifier of this ability score, which is equal to {@code (score value - 10) / 2}. For example,
     * a score of 8 or 9 has a modifier of -1, 10 or 11 of 0, 12 or 13 of 1,
     * and so on.
     *
     * @return the modifier of this ability score
     */
    public int getModifier() {
        return (getValue() - 10) / 2;
    }

    /**
     * Returns an abbreviation for the name of this ability score. For example, "Strength" is abbreviated as "STR".
     *
     * @return the abbreviated name of this ability score (e.g. "STR", usually referring to the modifier)
     */
    public LocalizedString getShortName() {
        return shortName;
    }

    /**
     * Returns the full name of this ability score (for example, "Strength").
     *
     * @return the name of this ability score
     */
    public LocalizedString getLongName() {
        return longName;
    }
}
