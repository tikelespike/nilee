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
 * This class is not intended to be used directly. Instead, use {@link PlayerCharacter#createSnapshot()} to create a
 * snapshot of the current character state. To restore a character from a snapshot, use
 * {@link PlayerCharacter#restoreSnapshot(PlayerCharacterSnapshot)}.
 * <p>
 * A snapshot <b>does</b> store all long-term persistent information about a character, such as their name or hit
 * points.
 * <p>
 * A snapshot <b>does not</b> store "short-term business object details" such as registered listeners or modifiers
 * applied to properties.
 */
@Entity
@Table(name = "application_player_character")
public final class PlayerCharacterSnapshot extends AbstractEntity {

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
    public PlayerCharacterSnapshot() {
        // Constructor should only be used by database framework or the PlayerCharacter class
    }

    User getOwner() {
        return owner;
    }

    void setOwner(User owner) {
        this.owner = owner;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    int getStrength() {
        return strength;
    }

    void setStrength(int strength) {
        this.strength = strength;
    }

    int getDexterity() {
        return dexterity;
    }

    void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    int getConstitution() {
        return constitution;
    }

    void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    int getIntelligence() {
        return intelligence;
    }

    void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    int getWisdom() {
        return wisdom;
    }

    void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    int getCharisma() {
        return charisma;
    }

    void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    int getHitPoints() {
        return hitPoints;
    }

    void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    int getTemporaryHitPoints() {
        return temporaryHitPoints;
    }

    void setTemporaryHitPoints(int temporaryHitPoints) {
        this.temporaryHitPoints = temporaryHitPoints;
    }

    Integer getHitPointMaxOverride() {
        return hitPointMaxOverride;
    }

    void setHitPointMaxOverride(Integer hitPointMaxOverride) {
        this.hitPointMaxOverride = hitPointMaxOverride;
    }
}
