package com.tikelespike.nilee.core.game;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GameSessionManager {
    private final Map<UUID, GameSession> sessions = new HashMap<>();

    public GameSession newSession() {
        GameSession session = new GameSession();
        sessions.put(session.getId(), session);
        return session;
    }

    public Optional<GameSession> getSession(UUID id) {
        return Optional.ofNullable(sessions.get(id));
    }

    public void endSession(UUID id) {
        sessions.remove(id);
    }
}
