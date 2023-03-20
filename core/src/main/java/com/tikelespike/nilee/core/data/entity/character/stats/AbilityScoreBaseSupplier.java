package com.tikelespike.nilee.core.data.entity.character.stats;

import com.tikelespike.nilee.core.data.entity.property.PropertyBaseSupplier;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class AbilityScoreBaseSupplier extends PropertyBaseSupplier<Integer> {

    @OneToOne
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
