package com.tikelespike.nilee.core.data.entity.property;

import javax.persistence.Entity;

@Entity
public class AdditiveModifier extends PropertyModifier<Integer> {

    private int offset;
    private String source;

    public AdditiveModifier() {
        this(0, "");
    }

    public AdditiveModifier(int offset, String source) {
        this.offset = offset;
        this.source = source;
    }

    @Override
    public Integer apply(Integer value) {
        return value == null ? null : value + offset;
    }

    @Override
    public String getAbstractDescription() {
        return getConcreteDescription();
    }

    @Override
    public String getConcreteDescription() {
        return "+ " + offset;
    }

    @Override
    public String getSourceName() {
        return source;
    }

    public void setOffset(int offset) {
        this.offset = offset;
        update();
    }

    public int getOffset() {
        return offset;
    }

    public void setSource(String source) {
        this.source = source;
        update();
    }

    public String getSource() {
        return source;
    }
}
