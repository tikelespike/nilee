package com.tikelespike.nilee.core.game;

/**
 * Represents a temporary time window where players come together play a game. A game session is a container for
 * short-lived game state shared between players. It thus enables communication between players currently playing
 * together.
 */
public class GameSession {

    private final RollBus rollBus = new RollBus();

    /**
     * @return the default bus on which rolls visible to all players sharing this session are made
     */
    public RollBus getRollBus() {
        return rollBus;
    }
}
