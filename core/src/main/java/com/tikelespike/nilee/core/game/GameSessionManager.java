package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.data.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.Optional;
import java.util.UUID;

/**
 * Manages {@link GameSession GameSessions}, a way for players to play together. Can be used to create new sessions,
 * join users to sessions, and retrieve a users current session, among other things.
 * <p>
 * Implementations of this interface should keep the following invariant:
 * Every user is always in exactly one session, i.e. {@link #getSessionOf(User)} should always be unambiguously defined.
 * Moreover, if a user leaves a session via {@link #leaveSession(User)}, they should automatically be joined to a new,
 * empty session.
 */
public interface GameSessionManager {

    /**
     * Opens a new game session, which is assigned a random, unique ID.
     *
     * @return the newly created game session
     */
    GameSession newSession();

    /**
     * Retrieves an existing game session by its ID, if it exists.
     *
     * @param id the ID of the session to retrieve
     * @return an optional containing the session with the given ID, or an empty optional if no such session exists
     */
    Optional<GameSession> getSession(UUID id);

    /**
     * Retrieves the session a user is currently participating in. As a user is always in exactly one session, this
     * will always return a non-null value.
     *
     * @param user the user to retrieve the session of
     * @return the session the user is currently in
     */
    @NotNull
    GameSession getSessionOf(User user);

    /**
     * Checks if a session with the given ID exists.
     *
     * @param id the ID of the session to check
     * @return true if a session with the given ID exists, false otherwise
     */
    default boolean hasSession(UUID id) {
        return getSession(id).isPresent();
    }

    /**
     * Add a user to an existing session.
     *
     * @param user the user joining the session
     * @param id   the ID of the session to join
     * @throws IllegalArgumentException if the session does not exist
     */
    void joinSession(User user, UUID id);

    /**
     * Remove a user from its current session. The user will automatically be joined to a new, empty session.
     *
     * @param user the user joining the session
     */
    void leaveSession(User user);
}
