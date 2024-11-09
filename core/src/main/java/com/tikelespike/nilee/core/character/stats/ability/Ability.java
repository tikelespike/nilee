package com.tikelespike.nilee.core.character.stats.ability;

import com.tikelespike.nilee.core.i18n.LocalizedString;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * The physical and mental capabilities of a character are modelled by six main abilities in D&D 5e: Strength,
 * Dexterity, Constitution, Intelligence, Wisdom, and Charisma. These abilities are quantified in {@link AbilityScores}
 * which assign a score from 1 to 30 to each ability.
 */
public enum Ability {
    /**
     * Strength measures bodily power, athletic training, and the extent to which a creature can exert raw physical
     * force. It is used for attack rolls, damage rolls, and many skills.
     */
    STRENGTH(t -> t.translate("core.character.ability.strength"), t -> t.translate("core.character.ability.str")),

    /**
     * Dexterity measures agility, reflexes, and balance. It is used for attack rolls, armor class, and many skills.
     */
    DEXTERITY(t -> t.translate("core.character.ability.dexterity"), t -> t.translate("core.character.ability.dex")),

    /**
     * Constitution represents a character's health, stamina, and vital force. It is used for hit points and for
     * concentrating on spells.
     */
    CONSTITUTION(t -> t.translate("core.character.ability.constitution"),
            t -> t.translate("core.character.ability.con")),

    /**
     * Intelligence measures mental acuity, accuracy of recall, and the ability to reason. It is used for many skills.
     */
    INTELLIGENCE(t -> t.translate("core.character.ability.intelligence"),
            t -> t.translate("core.character.ability.int")),

    /**
     * Wisdom reflects how attuned you are to the world around you and represents perceptiveness and intuition. It is
     * used for many skills.
     */
    WISDOM(t -> t.translate("core.character.ability.wisdom"), t -> t.translate("core.character.ability.wis")),

    /**
     * Charisma measures a character's ability to interact effectively with others. It includes such factors as
     * confidence and eloquence, and it can represent a charming or commanding personality.
     */
    CHARISMA(t -> t.translate("core.character.ability.charisma"), t -> t.translate("core.character.ability.cha"));

    private final LocalizedString longName;
    private final LocalizedString shortName;

    Ability(@NotNull LocalizedString longName, @NotNull LocalizedString shortName) {
        this.longName = Objects.requireNonNull(longName);
        this.shortName = Objects.requireNonNull(shortName);
    }

    /**
     * Returns an abbreviation for the name of this ability score. For example, "Strength" is abbreviated as "STR".
     *
     * @return the abbreviated name of this ability score (e.g. "STR", usually referring to the modifier)
     */
    public LocalizedString getShortName() {
        return shortName;
    }

    /**
     * Returns the full name of this ability score (for example, "Strength").
     *
     * @return the name of this ability score
     */
    public LocalizedString getLongName() {
        return longName;
    }
}
