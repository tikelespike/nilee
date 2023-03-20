package com.tikelespike.nilee.core.data.entity.character.stats;

import com.tikelespike.nilee.core.data.entity.property.Property;
import com.tikelespike.nilee.core.data.entity.property.PropertyBaseSupplier;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HPMaxBaseProperty extends Property<Integer> {

    protected HPMaxBaseProperty() {
    }

    public HPMaxBaseProperty(AbilityScore constitution) {
        super(new AbilityScoreBaseSupplier(constitution));
    }

}
