package com.tikelespike.nilee.core.data.entity.character.stats;

import com.tikelespike.nilee.core.data.entity.property.convenience.MaxValueSelector;
import com.tikelespike.nilee.core.data.entity.property.Property;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HitPointMax extends Property<Integer> {

    @OneToOne
    private HPMaxBaseProperty base;

    /**
     * Default constructor for JPA. Do not use.
     */
    protected HitPointMax() {
        setBaseValueSelector(new MaxValueSelector<>());
    }

    public HitPointMax(AbilityScore constitution) {
        this();
        base = new HPMaxBaseProperty(constitution);
        addBaseValueSupplier(new HPMaxBaseSupplier(base));
    }

    private void init() {
        base.addValueChangeListener(event -> notifyListeners());
    }

    public Property<Integer> getBaseValueProperty() {
        return base;
    }

    private void setBase(HPMaxBaseProperty base) {
        this.base = base;
    }

    private HPMaxBaseProperty getBase() {
        return base;
    }
}