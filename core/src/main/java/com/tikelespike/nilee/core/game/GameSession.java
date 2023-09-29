package com.tikelespike.nilee.core.game;

import java.util.UUID;

/**
 * Represents a temporary time window where players come together play a game. A game session is a container for
 * short-lived game state shared between players. It thus enables communication between players currently playing
 * together.
 */
public class GameSession {

    private final UUID id = java.util.UUID.randomUUID();
    private final RollBus rollBus = new RollBus();

    /**
     * Creates a new game session. Should only be used by {@link GameSessionManager}. Use
     * {@link GameSessionManager#newSession()} to create a new session.
     */
    protected GameSession() {
    }

    /**
     * @return the default bus on which rolls visible to all players sharing this session are made
     */
    public RollBus getRollBus() {
        return rollBus;
    }

    /**
     * @return the unique identifier of this game session
     */
    public UUID getId() {
        return id;
    }
}
