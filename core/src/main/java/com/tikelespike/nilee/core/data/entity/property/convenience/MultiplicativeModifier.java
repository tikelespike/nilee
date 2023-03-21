package com.tikelespike.nilee.core.data.entity.property.convenience;

import com.tikelespike.nilee.core.data.entity.property.PropertyModifier;

import javax.persistence.Entity;

@Entity
public class MultiplicativeModifier extends PropertyModifier<Integer> {

    private int factor;
    private String source;

    public MultiplicativeModifier() {
        this(1, "");
    }

    public MultiplicativeModifier(int factor, String source) {
        this.factor = factor;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value * factor;
    }

    @Override
    public String getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public String getConcreteDescription() {
        return "* " + factor;
    }

    @Override
    public String getSourceName() {
        return source;
    }

    public void setFactor(int factor) {
        this.factor = factor;
        update();
    }

    public int getFactor() {
        return factor;
    }

    public void setSource(String source) {
        this.source = source;
        update();
    }

    public String getSource() {
        return source;
    }
}
