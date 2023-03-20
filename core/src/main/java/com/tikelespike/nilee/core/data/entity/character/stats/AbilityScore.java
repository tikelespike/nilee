package com.tikelespike.nilee.core.data.entity.character.stats;

import com.tikelespike.nilee.core.data.entity.property.ConstantBaseProperty;

import javax.persistence.Entity;

@Entity
public class AbilityScore extends ConstantBaseProperty<Integer> {

    private static final int DEFAULT_VALUE = 10;

    private final String longName;
    private final String shortName;

    public static AbilityScore strength() {
        return new AbilityScore(DEFAULT_VALUE, "Strength", "STR");
    }

    public static AbilityScore dexterity() {
        return new AbilityScore(DEFAULT_VALUE, "Dexterity", "DEX");
    }

    public static AbilityScore constitution() {
        return new AbilityScore(DEFAULT_VALUE, "Constitution", "CON");
    }

    public static AbilityScore intelligence() {
        return new AbilityScore(DEFAULT_VALUE, "Intelligence", "INT");
    }

    public static AbilityScore wisdom() {
        return new AbilityScore(DEFAULT_VALUE, "Wisdom", "WIS");
    }

    public static AbilityScore charisma() {
        return new AbilityScore(DEFAULT_VALUE, "Charisma", "CHA");
    }

    protected AbilityScore() {
        this(DEFAULT_VALUE, "Unknown Ability Score", "UNK");
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
