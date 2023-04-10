package com.tikelespike.nilee.core.character.stats.hitpoints;

import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A base value supplier for the hit point max property, whose base value provided is described by its own property.
 * This allows modelling modifiers of the "base hit point maximum" (like CON and rolled hit dice values) separately
 * from higher-level modifiers of the hit point maximum (like temporary penalties caused by spells etc.).
 * <p>
 * This is an adapter class that allows a {@link Property} to be used as a {@link PropertyBaseSupplier}.
 */
public class HPMaxBaseSupplier extends PropertyBaseSupplier<Integer> {

    private final Property<Integer> hpMaxBaseProperty;

    /**
     * Creates a new {@link HPMaxBaseSupplier} using the given {@link Property} as its base value.
     *
     * @param hpMaxBaseProperty the {@link Property} the value of which is used as the base value of this supplier
     */
    public HPMaxBaseSupplier(@NotNull Property<Integer> hpMaxBaseProperty) {
        Objects.requireNonNull(hpMaxBaseProperty);
        this.hpMaxBaseProperty = hpMaxBaseProperty;
        this.hpMaxBaseProperty.addValueChangeListener(event -> update());
    }

    @Override
    public Integer getBaseValue() {
        return hpMaxBaseProperty.getValue();
    }

    @Override
    public String getAbstractDescription() {
        return "CON + rolled hit dice";
    }

    @Override
    public String getSourceName() {
        return "Base Hit Point Max";
    }
}
