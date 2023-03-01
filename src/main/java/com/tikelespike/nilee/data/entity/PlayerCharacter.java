package com.tikelespike.nilee.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tikelespike.nilee.data.entity.property.SDCIWCAttributes;

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
    private SDCIWCAttributes sdciwc;

    private String name;
    private int hitPoints = 10;
    private int maxHitPoints = 10;

    public PlayerCharacter() {
        sdciwc = new SDCIWCAttributes();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SDCIWCAttributes getSdciwc() {
        return sdciwc;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getOwner() {
        return owner;
    }


    public int getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(int maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public int getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
}
