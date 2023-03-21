package com.tikelespike.nilee.core.data.entity.property.convenience;

import com.tikelespike.nilee.core.data.entity.property.PropertyModifier;

import javax.persistence.Entity;

@Entity
public class AdditiveModifier extends PropertyModifier<Integer> {

    private int bonus;
    private String source;

    public AdditiveModifier() {
        this(0, "");
    }

    public AdditiveModifier(int bonus, String source) {
        this.bonus = bonus;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value + bonus;
    }

    @Override
    public String getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public String getConcreteDescription() {
        return "+ " + bonus;
    }

    @Override
    public String getSourceName() {
        return source;
    }

    public void setBonus(int offset) {
        this.bonus = offset;
        update();
    }

    public int getBonus() {
        return bonus;
    }

    public void setSource(String source) {
        this.source = source;
        update();
    }

    public String getSource() {
        return source;
    }
}
