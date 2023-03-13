package com.tikelespike.nilee.core.data.entity.property;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HitPointMax extends Property<Integer> {

    @OneToOne
    private HPMaxBase base;

    /**
     * Default constructor for JPA. Do not use.
     */
    protected HitPointMax() {
        setBaseValueSelector(new MaxValueSelector<>());
    }

    public HitPointMax(AbilityScore constitution) {
        this();
        base = new HPMaxBase(constitution);
        baseValueSuppliers.add(base);
    }

    private void init() {
        base.addValueChangeListener(event -> notifyListeners(getValueOnBase(event.getOldValue())));
    }

    public Property<Integer> getBaseValueProperty() {
        return base;
    }

    private void setBase(HPMaxBase base) {
        this.base = base;
    }

    private HPMaxBase getBase() {
        return base;
    }
}