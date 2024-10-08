package com.tikelespike.nilee.app.security;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.UserRepository;
import com.tikelespike.nilee.core.game.GameSessionManager;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Provides access to the currently authenticated user.
 */
@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private final GameSessionManager gameSessionManager;

    /**
     * Creates a new AuthenticatedUser. Should be created by Spring and injected where needed.
     *
     * @param authenticationContext the authentication context (injected by Spring)
     * @param userRepository the user repository (injected by Spring)
     * @param gameSessionManager the game session manager (injected by Spring)
     */
    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository,
                             GameSessionManager gameSessionManager) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
        this.gameSessionManager = gameSessionManager;
    }

    /**
     * If a user is authenticated, returns the {@link User} object for that user.
     *
     * @return the user currently authenticated, if any
     */
    @Transactional
    public Optional<User> get() {
        Optional<User> userOpt = authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
        userOpt.ifPresent(user -> user.setGameSessionManager(gameSessionManager));
        return userOpt;
    }

    /**
     * Logs out the currently authenticated user.
     */
    public void logout() {
        authenticationContext.logout();
    }

}
