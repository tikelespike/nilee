package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.dice.Dice;
import com.tikelespike.nilee.core.dice.DiceConstant;
import com.tikelespike.nilee.core.dice.DiceExpression;
import com.tikelespike.nilee.core.dice.DiceSum;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;

/**
 * The default way to calculate an ability check roll (for example, a strength check is 1d20 + strength modifier).
 *
 * @see <a href="https://www.dndbeyond.com/sources/basic-rules/using-ability-scores#AbilityChecks">Ability
 *         Checks</a>
 */
public class DefaultAbilityCheckRoll extends PropertyBaseSupplier<DiceExpression> {

    private final AbilityScore abilityScore;

    /**
     * Creates a new default ability check roll based on the given ability score.
     *
     * @param abilityScore the ability score to use when calculating the check roll
     */
    public DefaultAbilityCheckRoll(AbilityScore abilityScore) {
        this.abilityScore = abilityScore;
    }

    @Override
    public DiceExpression getBaseValue() {
        return new DiceSum(new Dice(20), new DiceConstant(abilityScore.getModifier()));
    }

    @Override
    public LocalizedString getAbstractDescription() {
        return t -> new Dice(20).toLocalizedString().getTranslation(t) + " " + t.translate("dice.sum.operator") + " "
                + abilityScore.getShortName().getTranslation(t);
    }

    @Override
    public LocalizedString getSourceName() {
        return t -> t.translate("core.character.ability.check.default");
    }
}
