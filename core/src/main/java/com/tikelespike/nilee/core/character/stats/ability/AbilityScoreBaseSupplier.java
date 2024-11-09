package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;
import jakarta.validation.constraints.NotNull;

/**
 * A specific {@link PropertyBaseSupplier} that provides the modifier value of a given {@link AbilityScore} as its base
 * value.
 */
public final class AbilityScoreBaseSupplier extends PropertyBaseSupplier<Integer> {

    private final AbilityScore abilityScore;

    /**
     * Creates a new {@link AbilityScoreBaseSupplier} using the given {@link AbilityScore}.
     *
     * @param abilityScore the {@link AbilityScore} the modifier value of which is used as the base value of
     *         this supplier
     */
    public AbilityScoreBaseSupplier(@NotNull AbilityScore abilityScore) {
        super(abilityScore);
        this.abilityScore = abilityScore;
    }

    @Override
    public Integer getBaseValue() {
        return abilityScore.getModifier();
    }

    @Override
    public LocalizedString getAbstractDescription() {
        return abilityScore.getAbility().getShortName();
    }

    @Override
    public LocalizedString getSourceName() {
        return abilityScore.getAbility().getLongName();
    }

    /**
     * Returns the {@link AbilityScore} the modifier value of which is used as the base value of this supplier.
     *
     * @return the {@link AbilityScore} the modifier value of which is used as the base value of this supplier
     */
    public AbilityScore getAbilityScore() {
        return abilityScore;
    }

}
