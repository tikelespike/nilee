package com.tikelespike.nilee.app.sessions;

import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.game.GameSession;
import com.tikelespike.nilee.core.game.GameSessionManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Manages the active {@link GameSession} for the current user.
 */
@Component
@Scope("session")
public class PlayerSessionManager implements DisposableBean {

    private final GameSessionManager gameSessionManager;
    private final User currentUser;

    private GameSession currentSession = null;

    protected PlayerSessionManager(SessionManagerWrapper gameSessionManager, AuthenticatedUser authenticatedUser) {
        this.gameSessionManager = gameSessionManager;
        this.currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not " +
                "authenticated"));
        joinSession(gameSessionManager.newSession().getId());
    }

    /**
     * @return the currently active session for the current user
     */
    public GameSession getCurrentSession() {
        return currentSession;
    }

    /**
     * Checks if a given session id is valid, that is, a session exists with that id.
     *
     * @param id the id to check
     * @return true if a session with the given id exists, false otherwise
     */
    public boolean isSessionIdValid(UUID id) {
        return gameSessionManager.hasSession(id);
    }

    /**
     * Joins an existing session. If the user is already in a session, they are removed from that session first.
     *
     * @param id the id of the session to join. Has to be valid, or an exception will be thrown.
     */
    public void joinSession(UUID id) {
        if (!gameSessionManager.hasSession(id)) throw new IllegalArgumentException("No session with id " + id);
        if (currentSession != null) leaveSession();

        currentSession = gameSessionManager.getSession(id).get();
        gameSessionManager.joinSession(id, currentUser);
    }

    /**
     * Leaves the current session and automatically rejoins a new, empty session.
     */
    public void joinNewSession() {
        joinSession(gameSessionManager.newSession().getId());
    }

    private void leaveSession() {
        gameSessionManager.leaveSession(currentSession.getId(), currentUser);
        currentSession = null;
    }

    @Override
    public void destroy() {
        gameSessionManager.leaveSession(currentSession.getId(), currentUser);
    }
}
