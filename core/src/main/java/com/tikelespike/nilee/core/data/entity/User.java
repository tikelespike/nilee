package com.tikelespike.nilee.core.data.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import com.tikelespike.nilee.core.game.GameSession;
import com.tikelespike.nilee.core.game.GameSessionManager;
import com.tikelespike.nilee.core.game.PlayerSessionManager;
import jakarta.persistence.*;

import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "application_user")
public class User extends AbstractEntity implements PlayerSessionManager {

    private String username;
    private String name;
    @JsonIgnore
    private String hashedPassword;
    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @Lob
    @Column(length = 1000000)
    private byte[] profilePicture;

    private Locale preferredLocale;

    @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER)
    private Set<PlayerCharacterSnapshot> characters;

    @Transient
    private GameSessionManager gameSessionManager;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Set<PlayerCharacterSnapshot> getCharacters() {
        return characters;
    }


    public Locale getPreferredLocale() {
        return preferredLocale;
    }

    public void setPreferredLocale(Locale preferredLocale) {
        this.preferredLocale = preferredLocale;
    }


    public GameSessionManager getGameSessionManager() {
        return gameSessionManager;
    }

    public void setGameSessionManager(GameSessionManager gameSessionManager) {
        this.gameSessionManager = gameSessionManager;
    }

    @Override
    public void joinSession(UUID id) {
        if (gameSessionManager == null) throw new IllegalStateException("Not connected to game session management");
        gameSessionManager.joinSession(this, id);
    }

    @Override
    public boolean canJoin(UUID id) {
        return gameSessionManager.hasSession(id);
    }

    @Override
    public GameSession getSession() {
        if (gameSessionManager == null) throw new IllegalStateException("Not connected to game session management");
        return gameSessionManager.getSessionOf(this);
    }

    @Override
    public void leaveCurrentSession() {
        if (gameSessionManager == null) throw new IllegalStateException("Not connected to game session management");
        gameSessionManager.leaveSession(this);
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
