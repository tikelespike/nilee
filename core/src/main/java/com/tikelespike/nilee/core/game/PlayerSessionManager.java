package com.tikelespike.nilee.core.game;

import java.util.UUID;

/**
 * Player-specific session management. Contains convenience methods based on {@link GameSessionManager} fixed
 * to a single user.
 */
public interface PlayerSessionManager {
    /**
     * Joins the session with the given id, if it exists. Throws an exception otherwise.
     *
     * @param id the id of the session to join
     * @throws IllegalArgumentException if no session with the stated id exists
     */
    void joinSession(UUID id);

    /**
     * Retrieve the session the user is currently participating in. A user is always in exactly one session.
     *
     * @return the session the player is currently in
     */
    GameSession getSession();

    /**
     * Leaves the session the user is currently participating in. The user will automatically be joined to a new,
     * empty session.
     */
    void leaveCurrentSession();
}
