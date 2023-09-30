package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.data.entity.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Manages {@link GameSession GameSessions} via a simple {@link Map} between ids and sessions.
 */
public class MapSessionManager implements GameSessionManager {

    private final Map<UUID, GameSession> sessions = new HashMap<>();

    @Override
    public GameSession newSession() {
        GameSession session = new GameSession();
        sessions.put(session.getId(), session);
        System.out.println("Created new session, now " + sessions.size() + " sessions");
        return session;
    }

    @Override
    public void joinSession(UUID id, User user) {
        if (!sessions.containsKey(id)) {
            throw new IllegalArgumentException("No session with id " + id);
        }
        sessions.get(id).addParticipant(user);
    }

    @Override
    public void leaveSession(UUID id, User user) {
        if (!hasSession(id)) {
            throw new IllegalArgumentException("No session with id " + id);
        }
        sessions.get(id).removeParticipant(user);
        System.out.println("Removed user from session, now " + sessions.get(id).getParticipants().size() + " users");
        if (sessions.get(id).getParticipants().isEmpty()) {
            endSession(id);
        }
    }

    @Override
    public Optional<GameSession> getSession(UUID id) {
        return Optional.ofNullable(sessions.get(id));
    }

    private void endSession(UUID id) {
        sessions.remove(id);
    }
}
