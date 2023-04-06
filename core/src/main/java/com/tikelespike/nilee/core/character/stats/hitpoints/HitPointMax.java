package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScoreBaseSupplier;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.PropertyModifier;
import com.tikelespike.nilee.core.property.convenience.ManualOverrideModifier;
import com.tikelespike.nilee.core.property.convenience.MaxValueSelector;

public class HitPointMax extends Property<Integer> {

    private Property<Integer> base;

    private AbilityScoreBaseSupplier abilityScoreBaseSupplier;

    private HPMaxBaseSupplier hpMaxBaseSupplier;

    private final ManualOverrideModifier<Integer> hpMaxOverride = new ManualOverrideModifier<>(0);

    /**
     * Default constructor for JPA. Do not use.
     */
    protected HitPointMax() {
        setBaseValueSelector(new MaxValueSelector<>());
    }

    public HitPointMax(AbilityScore constitution) {
        this();
        abilityScoreBaseSupplier = new AbilityScoreBaseSupplier(constitution);
        base = new Property<>(abilityScoreBaseSupplier);
        hpMaxBaseSupplier = new HPMaxBaseSupplier(base);
        addBaseValueSupplier(hpMaxBaseSupplier);
    }

    public void setOverride(Integer override) {
        if (override != null) {
            hpMaxOverride.setOverrideValue(override);
            if (!getModifiers().contains(hpMaxOverride)) {
                addModifier(hpMaxOverride);
            };
        } else {
            removeModifier(hpMaxOverride);
        }
    }

    public void removeOverride() {
        setOverride(null);
    }

    public Integer getOverride() {
        return getModifiers().contains(hpMaxOverride) ? hpMaxOverride.getOverrideValue() : null;
    }

    public Property<Integer> getBaseValueProperty() {
        return base;
    }
}