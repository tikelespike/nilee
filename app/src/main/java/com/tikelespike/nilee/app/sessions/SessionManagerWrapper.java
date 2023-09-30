package com.tikelespike.nilee.app.sessions;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.game.GameSession;
import com.tikelespike.nilee.core.game.GameSessionManager;
import com.tikelespike.nilee.core.game.MapSessionManager;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * A spring bean wrapper for {@link GameSessionManager}. Allows for easy singleton-style injection of a game
 * session manager.
 */
@Component
public class SessionManagerWrapper implements GameSessionManager {

    private final GameSessionManager gameSessionManager = new MapSessionManager();

    @Override
    public GameSession newSession() {
        return gameSessionManager.newSession();
    }

    @Override
    public void joinSession(UUID id, User user) {
        gameSessionManager.joinSession(id, user);
    }

    @Override
    public void leaveSession(UUID id, User user) {
        gameSessionManager.leaveSession(id, user);
    }

    @Override
    public Optional<GameSession> getSession(UUID id) {
        return gameSessionManager.getSession(id);
    }
}
