package com.tikelespike.nilee.app.security;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.UserRepository;
import com.tikelespike.nilee.core.game.GameSessionManager;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class AuthenticatedUser {

    private final UserRepository userRepository;
    private final AuthenticationContext authenticationContext;
    private final GameSessionManager gameSessionManager;

    public AuthenticatedUser(AuthenticationContext authenticationContext, UserRepository userRepository, GameSessionManager gameSessionManager) {
        this.userRepository = userRepository;
        this.authenticationContext = authenticationContext;
        this.gameSessionManager = gameSessionManager;
    }

    @Transactional
    public Optional<User> get() {
        Optional<User> userOpt = authenticationContext.getAuthenticatedUser(UserDetails.class)
                .map(userDetails -> userRepository.findByUsername(userDetails.getUsername()));
        userOpt.ifPresent(user -> user.setGameSessionManager(gameSessionManager));
        return userOpt;
    }

    public void logout() {
        authenticationContext.logout();
    }

}
