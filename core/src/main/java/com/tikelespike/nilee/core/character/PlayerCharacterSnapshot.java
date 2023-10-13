package com.tikelespike.nilee.core.character;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.data.entity.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

/**
 * A memento class of a {@link PlayerCharacter} that can stored in the database. Player character snapshots can be used
 * to save a characters state, optionally store it to the database and restore it later.
 * <p>
 * This class is not intended to be used directly. Instead, use {@link PlayerCharacter#createSnapshot()} to create a snapshot of
 * the current character state. To restore a character from a snapshot, use {@link PlayerCharacter#restoreSnapshot(PlayerCharacterSnapshot)}.
 * <p>
 * A snapshot <b>does</b> store all long-term persistent information about a character, such as their name, spell slots, hit points,
 * and other "character-building" information (class choices, ...). (TODO: Note: most of this is not yet implemented.)
 * <p>
 * A snapshot does <b>not</b> store "short-term business object details" such as registered listeners or modifiers applied to
 * properties.
 */
@Entity
@Table(name = "application_player_character")
public class PlayerCharacterSnapshot extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @NotNull
    @JsonIgnoreProperties({"characters"})
    private User owner;

    private String name;

    private int strength;
    private int dexterity;
    private int constitution;
    private int intelligence;
    private int wisdom;
    private int charisma;

    private int hitPoints;
    private int temporaryHitPoints;
    private Integer hitPointMaxOverride;

    /**
     * Creates a new player character snapshot with uninitialized values. A snapshot should only be created by
     * {@link PlayerCharacter#createSnapshot()}.
     */
    protected PlayerCharacterSnapshot() {
    }

    protected User getOwner() {
        return owner;
    }

    protected void setOwner(User owner) {
        this.owner = owner;
    }

    protected String getName() {
        return name;
    }

    protected void setName(String name) {
        this.name = name;
    }

    protected int getStrength() {
        return strength;
    }

    protected void setStrength(int strength) {
        this.strength = strength;
    }

    protected int getDexterity() {
        return dexterity;
    }

    protected void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    protected int getConstitution() {
        return constitution;
    }

    protected void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    protected int getIntelligence() {
        return intelligence;
    }

    protected void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    protected int getWisdom() {
        return wisdom;
    }

    protected void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    protected int getCharisma() {
        return charisma;
    }

    protected void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    protected int getHitPoints() {
        return hitPoints;
    }

    protected void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    protected int getTemporaryHitPoints() {
        return temporaryHitPoints;
    }

    protected void setTemporaryHitPoints(int temporaryHitPoints) {
        this.temporaryHitPoints = temporaryHitPoints;
    }

    protected Integer getHitPointMaxOverride() {
        return hitPointMaxOverride;
    }

    protected void setHitPointMaxOverride(Integer hitPointMaxOverride) {
        this.hitPointMaxOverride = hitPointMaxOverride;
    }
}
