package com.tikelespike.nilee.core.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import com.tikelespike.nilee.core.game.GameSession;
import com.tikelespike.nilee.core.game.GameSessionManager;
import com.tikelespike.nilee.core.game.PlayerSessionManager;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

/**
 * An account of a nilee application user. A user can have multiple player characters that represent the made-up in-game
 * people, while the user object represents the person playing (or their account, to be more precise).
 */
@Entity
@Table(name = "application_user")
public class User extends AbstractEntity implements PlayerSessionManager {

    private static final int MAX_PROFILE_PICTURE_SIZE_BYTES = 1000000;
    private String username;
    private String name;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Lob
    @Column(length = MAX_PROFILE_PICTURE_SIZE_BYTES)
    private byte[] profilePicture;

    private Locale preferredLocale;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<PlayerCharacterSnapshot> characters;

    @Transient
    private GameSessionManager gameSessionManager;

    /**
     * @return the unique username identifying this user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the unique username identifying this user
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the not necessarily unique display name for this account chosen by the user (as opposed to the unique
     * username returned by {@link #getUsername()}. This display name can be the full real-life name of the user, but it
     * can also be a pseudonym.
     *
     * @return the users chosen display name (not necessarily unique)
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the not necessarily unique display name for this account chosen by the user (as opposed to the unique
     * username returned by {@link #getUsername()}. This display name can be the full real-life name of the user, but it
     * can also be a pseudonym.
     *
     * @param name the users chosen display name (not necessarily unique)
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the password hash of this users password
     */
    public String getHashedPassword() {
        return hashedPassword;
    }

    /**
     * @param hashedPassword the password hash of this users password
     */
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    /**
     * Returns the roles this user has. Roles define sets of permissions (for example, an admin user has heightened
     * privileges compared to a standard user).
     *
     * @return the roles this user has
     */
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Sets the roles this user has. Roles define sets of permissions (for example, an admin user has heightened
     * privileges compared to a standard user).
     *
     * @param roles the roles this user has
     */
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    /**
     * @return the profile picture of this user as a byte array
     */
    public byte[] getProfilePicture() {
        return profilePicture;
    }

    /**
     * @param profilePicture the profile picture of this user as a byte array
     */
    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    /**
     * Returns the set of player characters owned by this user. More precisely, returns a set of character snapshots
     * (serializable external state mementos of the respective characters). Use
     * {@link com.tikelespike.nilee.core.character.PlayerCharacter#createFromSnapshot(PlayerCharacterSnapshot)} to
     * create a full business object.
     *
     * @return the set of player character snapshots this user owns
     */
    public Set<PlayerCharacterSnapshot> getCharacters() {
        return characters;
    }

    /**
     * @return the locale this user prefers their UI to be displayed in
     */
    public Locale getPreferredLocale() {
        return preferredLocale;
    }

    /**
     * @param preferredLocale the locale this user prefers their UI to be displayed in
     */
    public void setPreferredLocale(Locale preferredLocale) {
        this.preferredLocale = preferredLocale;
    }


    /**
     * Returns the session management this user is currently connected to, or null if not connected. The session manager
     * allows this user to play together with other users connected to the same session manager by joining a shared
     * session.
     *
     * @return the session management this user is currently connected to, or null if not connected
     */
    public GameSessionManager getGameSessionManager() {
        return gameSessionManager;
    }

    /**
     * Connects this user to session management (or disconnects if set to null). The session manager allows this user to
     * play together with other users connected to the same session manager by joining a shared session.
     *
     * @param gameSessionManager the global session management allowing this user to play together with other
     */
    public void setGameSessionManager(GameSessionManager gameSessionManager) {
        this.gameSessionManager = gameSessionManager;
    }

    @Override
    public void joinSession(UUID id) {
        ensureConnectedToSessionManager();
        gameSessionManager.joinSession(this, id);
    }

    @Override
    public boolean canJoin(UUID id) {
        ensureConnectedToSessionManager();
        return gameSessionManager.hasSession(id);
    }

    @Override
    public GameSession getSession() {
        ensureConnectedToSessionManager();
        return gameSessionManager.getSessionOf(this);
    }

    @Override
    public void leaveCurrentSession() {
        ensureConnectedToSessionManager();
        gameSessionManager.leaveSession(this);
    }

    private void ensureConnectedToSessionManager() {
        if (gameSessionManager == null) {
            throw new IllegalStateException("Not connected to game session management");
        }
    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User other)) {
            return false; // null or other class
        }

        if (getId() != null) {
            return getId().equals(other.getId());
        }
        return super.equals(other);
    }
}
