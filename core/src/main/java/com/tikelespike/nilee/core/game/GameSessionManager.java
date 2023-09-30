package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.data.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Manages {@link GameSession GameSessions}, a way for players to play together. Can be used to create new sessions,
 * retrieve existing sessions, and end sessions.
 */
public interface GameSessionManager {

    /**
     * Opens a new game session, which is assigned a random ID.
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
     * @param id   the ID of the session to join
     * @param user the user joining the session
     * @throws IllegalArgumentException if the session does not exist
     */
    void joinSession(UUID id, User user);

    /**
     * Remove a user from an existing session. Will do nothing if the user is not in the session.
     *
     * @param id   the ID of the session to join
     * @param user the user joining the session
     * @throws IllegalArgumentException if the session does not exist
     */
    void leaveSession(UUID id, User user);
}
