package com.tikelespike.nilee.core.data.entity.character;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tikelespike.nilee.core.data.entity.AbstractEntity;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.entity.character.stats.ability.AbilityScores;
import com.tikelespike.nilee.core.data.entity.character.stats.hitpoints.HitPoints;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "application_player_character")
public class PlayerCharacter extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @NotNull
    @JsonIgnoreProperties({"characters"})
    private User owner;

    @OneToOne(cascade = CascadeType.ALL)
    private AbilityScores abilityScores;

    @OneToOne(cascade = CascadeType.ALL)
    private HitPoints hitPoints;

    private String name;

    protected PlayerCharacter() {
        abilityScores = new AbilityScores();
        name = getDefaultName();
        hitPoints = new HitPoints(abilityScores.getConstitution());
    }

    public PlayerCharacter(User owner) {
        this();
        this.owner = owner;
    }

    public PlayerCharacter(String name, User owner) {
        this(owner);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbilityScores getAbilityScores() {
        return abilityScores;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }

    public HitPoints getHitPoints() {
        return hitPoints;
    }

    private String getDefaultName() {
        return "Unnamed Character #" + getId();
    }
}
