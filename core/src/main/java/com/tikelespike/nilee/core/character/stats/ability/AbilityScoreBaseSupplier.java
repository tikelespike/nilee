package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.property.PropertyBaseSupplier;

public class AbilityScoreBaseSupplier extends PropertyBaseSupplier<Integer> {

    private AbilityScore abilityScore;

    protected AbilityScoreBaseSupplier() {
    }

    public AbilityScoreBaseSupplier(AbilityScore abilityScore) {
        setAbilityScore(abilityScore);
    }

    @Override
    public Integer getBaseValue() {
        return abilityScore.getModifier();
    }

    @Override
    public String getAbstractDescription() {
        return abilityScore.getShortName();
    }

    @Override
    public String getSourceName() {
        return abilityScore.getLongName();
    }

    public AbilityScore getAbilityScore() {
        return abilityScore;
    }

    private void setAbilityScore(AbilityScore abilityScore) {
        this.abilityScore = abilityScore;
        abilityScore.addValueChangeListener(event -> update());
    }
}
