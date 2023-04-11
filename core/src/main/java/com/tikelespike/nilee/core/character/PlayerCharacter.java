package com.tikelespike.nilee.core.character;

import com.tikelespike.nilee.core.character.stats.ability.AbilityScores;
import com.tikelespike.nilee.core.character.stats.hitpoints.HitPoints;
import com.tikelespike.nilee.core.data.entity.User;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * A player character represents a player's representative in the game, their avatar in the D&D world.
 * In the game, a character is a combination of game statistics, role-playing hooks, and the player's imagination.
 * This class tracks all information (mostly game statistics) of one character, such as their
 * <ul>
 *     <li>name</li>
 *     <li>owning player</li>
 *     <li>ability scores, describing physical and mental characteristics of the character</li>
 *     <li>hit points, describing their health and ability to withstand damage</li>
 *     <li>class, describing their specific role in the party</li>
 *     <li>race, describing their specific physical characteristics</li>
 * </ul>
 * ... and many more.
 * <p>
 * This class implements the Memento design pattern to allow for saving and loading of so-called
 * {@link PlayerCharacterSnapshot snapshots}. These can be persisted to a database. A player character can be created
 * from a snapshot using {@link #createFromSnapshot(PlayerCharacterSnapshot)}, or an existing character can load a
 * snapshot using {@link #restoreSnapshot(PlayerCharacterSnapshot)}.
 *
 * @see <a href="https://www.dndbeyond.com/sources/basic-rules/step-by-step-characters">Creating a Character (D&D Beyond)</a>
 */
public class PlayerCharacter {

    private final AbilityScores abilityScores;
    private final HitPoints hitPoints;

    // unique identifier for corresponding database snapshots
    private Long id;
    private User owner;
    private String name;

    // version of the snapshot this character was loaded from, to keep track of which snapshot this character
    // was loaded from
    private int loadedFromVersion;

    /**
     * Creates a new player character with uninitialized values. Should only be used for loading from snapshots.
     */
    protected PlayerCharacter() {
        abilityScores = new AbilityScores();
        name = getDefaultName();
        hitPoints = new HitPoints(abilityScores.getConstitution());
    }

    /**
     * Creates a new player character with the given owner and a generated default name.
     *
     * @param owner the user who can control this character
     */
    public PlayerCharacter(@NotNull User owner) {
        this();
        Objects.requireNonNull(owner);
        this.owner = owner;
    }

    /**
     * Creates a new player character with the given name and owner.
     *
     * @param name  the name of the character
     * @param owner the user who can control this character
     */
    public PlayerCharacter(@NotNull String name, @NotNull User owner) {
        this(owner);
        Objects.requireNonNull(name);
        this.name = name;
    }

    /**
     * Creates a new player character object from the given snapshot.
     *
     * @param snapshot the snapshot containing stored data with which to initialize the new character
     * @return a new player character object initialized with the data from the given snapshot
     */
    public static PlayerCharacter createFromSnapshot(@NotNull PlayerCharacterSnapshot snapshot) {
        Objects.requireNonNull(snapshot);
        PlayerCharacter bo = new PlayerCharacter();
        bo.restoreSnapshot(snapshot);
        return bo;
    }

    /**
     * Creates a snapshot storing the data of the current character. This snapshot implements the memento design
     * pattern, and it can also be persisted to a database.
     * It can also be used to "undo" changes to the character by restoring the snapshot.
     *
     * @return a snapshot storing the data of the current character
     */
    public PlayerCharacterSnapshot createSnapshot() {
        PlayerCharacterSnapshot snapshot = new PlayerCharacterSnapshot();
        snapshot.setVersion(this.getLoadedFromVersion());
        snapshot.setId(this.getId());
        snapshot.setOwner(this.getOwner());
        snapshot.setName(this.getName());
        snapshot.setStrength(this.getAbilityScores().getStrength().getDefaultBaseValue());
        snapshot.setConstitution(this.getAbilityScores().getConstitution().getDefaultBaseValue());
        snapshot.setHitPoints(this.getHitPoints().getCurrentHitPoints());
        snapshot.setTemporaryHitPoints(this.getHitPoints().getTemporaryHitPoints());
        snapshot.setHitPointMaxOverride(this.getHitPoints().getMaxHitPoints().getOverride());
        return snapshot;
    }

    /**
     * Loads the data in the given snapshot into this character. This can be used to "undo" changes made to this
     * character since creating the snapshot.
     *
     * @param snapshot the snapshot containing the data to load into this character
     */
    public void restoreSnapshot(@NotNull PlayerCharacterSnapshot snapshot) {
        Objects.requireNonNull(snapshot);
        loadedFromVersion = snapshot.getVersion();
        id = snapshot.getId();
        owner = snapshot.getOwner();
        name = snapshot.getName();
        abilityScores.getStrength().setDefaultBaseValue(snapshot.getStrength());
        abilityScores.getConstitution().setDefaultBaseValue(snapshot.getConstitution());
        hitPoints.getMaxHitPoints().setOverride(snapshot.getHitPointMaxOverride());
        hitPoints.setCurrentHitPoints(snapshot.getHitPoints());
        hitPoints.setTemporaryHitPoints(snapshot.getTemporaryHitPoints());
    }

    /**
     * @return the name of this character (e.g. "Akaryn Moongrey"), or a default name if no name has been set
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name of this character (e.g. "Akaryn Moongrey")
     */
    public void setName(@NotNull String name) {
        Objects.requireNonNull(name);
        this.name = name;
    }

    /**
     * Returns the ability scores (strength, dexterity, constitution, intelligence, wisdom, charisma) of this character.
     *
     * @return the ability scores of this character
     */
    public AbilityScores getAbilityScores() {
        return abilityScores;
    }

    /**
     * @return the unique identifier of this character, or null if this character has not been saved to a database yet
     */
    public Long getId() {
        return id;
    }

    /**
     * @return the user who can control this character
     */
    public User getOwner() {
        return owner;
    }

    /**
     * @param owner the user who can control this character
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }

    /**
     * Returns the hit points of this character. This includes the current hit points, the maximum hit points, and
     * temporary hit points.
     *
     * @return the hit points of this character
     * @see HitPoints
     */
    public HitPoints getHitPoints() {
        return hitPoints;
    }

    /**
     * @return the (persistence) version of the snapshot this character was loaded from, or 0 if this character has not
     * been loaded from a snapshot
     */
    public int getLoadedFromVersion() {
        return loadedFromVersion;
    }

    private String getDefaultName() {
        return "Unnamed Character #" + id;
    }
}
