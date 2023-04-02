package com.tikelespike.nilee.core.character;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScores;
import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.tikelespike.nilee.core.data.entity.User;

public class PlayerCharacter {

    private Long id;

    private User owner;

    private final AbilityScores abilityScores;

    private final HitPoints hitPoints;

    private String name;

    private int loadedFromVersion;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return "Unnamed Character #" + id;
    }

    public int getLoadedFromVersion() {
        return loadedFromVersion;
    }

    public void setLoadedFromVersion(int loadedFromVersion) {
        this.loadedFromVersion = loadedFromVersion;
    }
}
