package com.tikelespike.nilee.data.entity.property;

import com.tikelespike.nilee.data.entity.AbstractEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class SDCIWCAttributes extends AbstractEntity {

    public static final int DEFAULT_STRENGTH = 10;

    @OneToOne(cascade = CascadeType.ALL)
    private Property<Integer> strengthProperty;

    @OneToOne(cascade = CascadeType.ALL)
    private ConstantBaseValue baseStrength;


    public SDCIWCAttributes() {
        this.baseStrength = new ConstantBaseValue(DEFAULT_STRENGTH, "Base Strength");
        this.strengthProperty = new Property<>(this.baseStrength);
    }

    public int getBaseStrength() {
        return baseStrength.getBaseValue();
    }

    public void setBaseStrength(int baseStrength) {
        this.baseStrength.setBaseValue(baseStrength);
    }

    public Property<Integer> getStrengthProperty() {
        return strengthProperty;
    }
}
