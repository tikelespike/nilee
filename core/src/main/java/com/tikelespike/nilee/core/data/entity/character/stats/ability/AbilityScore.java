package com.tikelespike.nilee.core.data.entity.character.stats.ability;

import com.tikelespike.nilee.core.data.entity.property.convenience.ConstantBaseProperty;

import javax.persistence.Entity;

@Entity
public class AbilityScore extends ConstantBaseProperty {

    private final String longName;
    private final String shortName;

    protected AbilityScore() {
        this(0, "Unknown Ability Score", "UNK");
    }

    public AbilityScore(int defaultBase, String longName, String shortName) {
        super(defaultBase, longName);
        this.longName = longName;
        this.shortName = shortName;
    }

    public int getModifier() {
        return (getValue() - 10) / 2;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }
}
