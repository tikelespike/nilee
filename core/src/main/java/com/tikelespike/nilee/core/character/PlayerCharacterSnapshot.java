package com.tikelespike.nilee.core.character;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tikelespike.nilee.core.character.classes.ClassInstanceEntity;
import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.data.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * A memento class of a {@link PlayerCharacter} that can stored in the database. Player character snapshots can be used
 * to save a characters state, optionally store it to the database and restore it later. It can also be understood as a
 * "data transfer object" (DTO) for a player character.
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ClassInstanceEntity> classes;

    /**
     * Creates a new player character snapshot with uninitialized values. A snapshot should only be created by
     * {@link PlayerCharacter#createSnapshot()}.
     */
    public PlayerCharacterSnapshot() {
        // Constructor should only be used by database framework or the PlayerCharacter class
    }

    // Getter and setter methods are required for JPA. Documentation is omitted for brevity. See the business object for
    // more information.
    //CHECKSTYLE.OFF: JavadocMethod

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getConstitution() {
        return constitution;
    }

    public void setConstitution(int constitution) {
        this.constitution = constitution;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public int getWisdom() {
        return wisdom;
    }

    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }

    public int getCharisma() {
        return charisma;
    }

    public void setCharisma(int charisma) {
        this.charisma = charisma;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }

    public int getTemporaryHitPoints() {
        return temporaryHitPoints;
    }

    public void setTemporaryHitPoints(int temporaryHitPoints) {
        this.temporaryHitPoints = temporaryHitPoints;
    }

    public Integer getHitPointMaxOverride() {
        return hitPointMaxOverride;
    }

    public void setHitPointMaxOverride(Integer hitPointMaxOverride) {
        this.hitPointMaxOverride = hitPointMaxOverride;
    }

    public List<ClassInstanceEntity> getClasses() {
        return classes;
    }

    public void setClasses(List<ClassInstanceEntity> classes) {
        this.classes = classes;
    }

    //CHECKSTYLE.ON: JavadocMethod
}
