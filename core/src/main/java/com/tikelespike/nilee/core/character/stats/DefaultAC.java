package com.tikelespike.nilee.core.character.stats;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;

/**
 * The default armor class for a character, computed as 10 + DEX.
 */
public class DefaultAC extends PropertyBaseSupplier<Integer> {

    private final AbilityScore dex;

    /**
     * Creates a new default armor class value supplier.
     *
     * @param dex the dexterity property to use for computing the armor class, should be the one of the
     *         character whose armor class this is
     */
    public DefaultAC(AbilityScore dex) {
        this.dex = dex;
    }

    @Override
    public Integer getBaseValue() {
        return 10 + dex.getModifier();
    }

    @Override
    public LocalizedString getAbstractDescription() {
        return t -> "10 + " + t.translate("core.character.ability.dex");
    }

    @Override
    public LocalizedString getSourceName() {
        return t -> t.translate("core.character.armor_class.default");
    }
}
