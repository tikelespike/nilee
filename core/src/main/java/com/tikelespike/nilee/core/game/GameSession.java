package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.data.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a temporary time window where players come together play a game. A game session is a container for
 * short-lived game state shared between players. It thus enables communication between players currently playing
 * together.
 */
public class GameSession {

    private final UUID id = java.util.UUID.randomUUID();
    private final RollBus rollBus = new RollBus();

    private final List<User> participants = new ArrayList<>();

    /**
     * Creates a new game session. Should only be used by implementations of {@link GameSessionManager}. Use
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

    /**
     * @param participant the user to add to this session
     */
    protected void addParticipant(User participant) {
        participants.add(participant);
    }

    /**
     * Removes a user from this session. Will do nothing if the user is not in this session in the first place.
     *
     * @param participant the user to remove from this session
     * @return true if and only if the participant was in this session before calling this method
     */
    protected boolean removeParticipant(User participant) {
        return participants.remove(participant);
    }

    /**
     * @return an unmodifiable copy of the list of all participants in this session
     */
    public List<User> getParticipants() {
        return List.copyOf(participants);
    }

    @Override
    public String toString() {
        return getId().toString();
    }
}
