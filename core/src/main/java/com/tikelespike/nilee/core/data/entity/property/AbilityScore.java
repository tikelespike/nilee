package com.tikelespike.nilee.core.data.entity.property;

import javax.persistence.Entity;

@Entity
public class AbilityScore extends ConstantBaseProperty<Integer> {

    protected AbilityScore() {
        super(10, "Unknown Ability Score");
    }

    public AbilityScore(int defaultBase, String description) {
        super(defaultBase, description);
    }

    public int getModifier() {
        return (getValue() - 10) / 2;
    }
}
