package com.tikelespike.nilee.core.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tikelespike.nilee.core.character.PlayerCharacter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "application_player_character")
public class PlayerCharacterDTO extends AbstractEntity {

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

    public PlayerCharacterDTO() {
    }

    public static PlayerCharacterDTO fromBO(PlayerCharacter bo) {
        PlayerCharacterDTO dto = new PlayerCharacterDTO();
        dto.setVersion(bo.getLoadedFromVersion());
        dto.setId(bo.getId());
        dto.setOwner(bo.getOwner());
        dto.setName(bo.getName());
        dto.setStrength(bo.getAbilityScores().getStrength().getDefaultBaseValue());
        dto.setConstitution(bo.getAbilityScores().getConstitution().getDefaultBaseValue());
        dto.setHitPoints(bo.getHitPoints().getCurrentHitPoints());
        dto.setTemporaryHitPoints(bo.getHitPoints().getTemporaryHitPoints());
        return dto;
    }

    public PlayerCharacter toBO() {
        PlayerCharacter bo = new PlayerCharacter(this.owner);
        bo.setLoadedFromVersion(getVersion());
        bo.setId(getId());
        bo.setName(this.name);
        bo.getAbilityScores().getStrength().setDefaultBaseValue(this.strength);
        bo.getAbilityScores().getConstitution().setDefaultBaseValue(this.constitution);
        bo.getHitPoints().setCurrentHitPoints(this.hitPoints);
        bo.getHitPoints().setTemporaryHitPoints(this.temporaryHitPoints);
        return bo;
    }

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
}
