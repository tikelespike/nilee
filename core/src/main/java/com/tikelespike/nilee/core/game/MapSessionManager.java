package com.tikelespike.nilee.core.game;

import com.tikelespike.nilee.core.data.entity.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Manages {@link GameSession GameSessions} via a simple {@link Map} between ids and sessions and keeps
 * track of the session a user is currently in via another Map. Keeps the invariant that every user is always in
 * exactly one session, as well as removing empty sessions.
 */
public class MapSessionManager implements GameSessionManager {

    private final Map<UUID, GameSession> sessions = new ConcurrentHashMap<>();
    private final Map<User, UUID> lastUserSessions = new ConcurrentHashMap<>();

    @Override
    public GameSession newSession() {
        GameSession session = new GameSession();
        // since uuid is 128bit, we can safely assume the new session id is not already in use
        sessions.put(session.getId(), session);
        return session;
    }

    @Override
    public synchronized void joinSession(User user, UUID id) {
        ensureSessionValidity(id);
        UUID lastSession = lastUserSessions.get(user);
        if (hasSession(lastSession)) removeUserFromSession(lastSession, user);
        sessions.get(id).addParticipant(user);
        lastUserSessions.put(user, id);
    }

    @Override
    public synchronized void leaveSession(User user) {
        initUser(user);
    }

    @Override
    public synchronized GameSession getSessionOf(User user) {
        return getSession(lastUserSessions.get(user)).orElseGet(() -> initUser(user));
    }

    @Override
    public synchronized Optional<GameSession> getSession(UUID id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(sessions.get(id));
    }

    private synchronized void removeUserFromSession(UUID id, User user) {
        ensureSessionValidity(id);
        sessions.get(id).removeParticipant(user);
        if (sessions.get(id).getParticipants().isEmpty()) {
            endSession(id);
        }
    }

    private void ensureSessionValidity(UUID id) {
        if (!hasSession(id)) {
            throw new IllegalArgumentException("No session with id " + id);
        }
    }

    private void endSession(UUID id) {
        sessions.remove(id);
    }

    private synchronized GameSession initUser(User user) {
        GameSession session = newSession();
        joinSession(user, session.getId());
        return session;
    }
}
