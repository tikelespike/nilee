package com.tikelespike.nilee.core.character.stats;

import com.tikelespike.nilee.core.character.classes.CharacterClass;
import com.tikelespike.nilee.core.i18n.LocalizedString;
import com.tikelespike.nilee.core.property.Property;
import com.tikelespike.nilee.core.property.PropertyBaseSupplier;

import java.util.List;
import java.util.stream.Collectors;

/**
 * The total level of a character, accumulated across all classes.
 */
public class TotalCharacterLevel extends Property<Integer> {
    /**
     * Creates a new total character level property with the default calculation method.
     *
     * @param classes the classes of the character
     */
    public TotalCharacterLevel(List<CharacterClass> classes) {
        super(new PropertyBaseSupplier<>() {
            @Override
            public Integer getBaseValue() {
                return classes.stream().mapToInt(CharacterClass::getLevel).sum();
            }

            @Override
            public LocalizedString getAbstractDescription() {
                return t -> classes.stream()
                        .map(c -> t.translate("core.character.classes.total_level_calculation_step", c.getLevel(),
                                c.getName().getTranslation(t)))
                        .collect(Collectors.joining(" " + t.translate("generic.sum.operator") + " "));
            }

            @Override
            public LocalizedString getSourceName() {
                return t -> t.translate("core.character.classes.total_level_default_description");
            }
        });
    }
}
