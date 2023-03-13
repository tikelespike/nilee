package com.tikelespike.nilee.core.data.entity.property;

import com.tikelespike.nilee.core.data.entity.GameEntity;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
public class HPMaxBase extends Property<Integer> implements PropertyBaseSupplier<Integer> {

    protected HPMaxBase() {
    }

    public HPMaxBase(AbilityScore constitution) {
        super(new ConBaseValue(constitution));
    }

    @Override
    public Integer getBaseValue() {
        return getValue();
    }

    @Override
    public String getAbstractDescription() {
        return "CON + rolled hit dice";
    }

    @Override
    public String getSourceName() {
        return "Base Hit Point Max";
    }


    @Entity
    private static class ConBaseValue extends GameEntity implements PropertyBaseSupplier<Integer> {

        @OneToOne
        private AbilityScore constitution;

        protected ConBaseValue() {
        }

        public ConBaseValue(AbilityScore constitution) {
            this.constitution = constitution;
        }

        @Override
        public Integer getBaseValue() {
            return constitution.getModifier();
        }

        @Override
        public String getAbstractDescription() {
            return "CON";
        }

        @Override
        public String getSourceName() {
            return "Constitution";
        }
    }
}
