package com.tikelespike.nilee.app.sessions;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.game.GameSession;
import com.tikelespike.nilee.core.game.GameSessionManager;
import com.tikelespike.nilee.core.game.MapSessionManager;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * A spring bean wrapper for {@link GameSessionManager}. Allows for easy singleton-style injection of a game session
 * manager.
 */
@Component
public class SessionManagerWrapper implements GameSessionManager {

    private final GameSessionManager gameSessionManager = new MapSessionManager();

    @Override
    public GameSession newSession() {
        return gameSessionManager.newSession();
    }

    @Override
    public Optional<GameSession> getSession(UUID id) {
        return gameSessionManager.getSession(id);
    }

    @Override
    public GameSession getSessionOf(User user) {
        return gameSessionManager.getSessionOf(user);
    }

    @Override
    public void joinSession(User user, UUID id) {
        gameSessionManager.joinSession(user, id);
    }

    @Override
    public void leaveSession(User user) {
        gameSessionManager.leaveSession(user);
    }
}
