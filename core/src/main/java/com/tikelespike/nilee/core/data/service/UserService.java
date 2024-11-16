package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.game.GameSessionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service used to store and obtain {@link User Users} to/from the database.
 */
@Service
public class UserService {

    private final UserRepository repository;
    private final GameSessionManager gameSessionManager;


    /**
     * Creates a new UserService accessing the database using the given JPA repository.
     *
     * @param repository the repository used to access the database
     * @param gameSessionManager the game session manager used to manage shared game sessions
     */
    public UserService(UserRepository repository, GameSessionManager gameSessionManager) {
        this.repository = repository;
        this.gameSessionManager = gameSessionManager;
    }

    /**
     * Retrieves a user by its unique id.
     *
     * @param id unique numerical identifier of the user
     *
     * @return an optional containing the corresponding user object, or an empty optional if the id could not be found
     *         in the databases
     */
    @Transactional
    public Optional<User> get(Long id) {
        Optional<User> optionalUser = repository.findById(id);
        optionalUser.ifPresent(this::initializeUser);
        return optionalUser;
    }

    /**
     * Retrieves a user by its unique username.
     *
     * @param username the unique username of the user to retrieve
     *
     * @return an optional containing the corresponding user object, or an empty optional if the username could not be
     *         found in the database
     */
    @Transactional
    public Optional<User> get(String username) {
        Optional<User> optionalUser = Optional.ofNullable(repository.findByUsername(username));
        optionalUser.ifPresent(this::initializeUser);
        return optionalUser;
    }

    /**
     * Stores a user in the database.
     *
     * @param entity the user to store
     *
     * @return the saved user
     */
    @Transactional
    public User update(User entity) {
        return initializeUser(repository.save(entity));
    }

    /**
     * Deletes a user from the database.
     *
     * @param id the id of the user to delete
     */
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    private User initializeUser(User user) {
        user.setGameSessionManager(gameSessionManager);

        // initialize characters and classes eagerly to avoid lazy loading issues later
        user.getCharacters().forEach(c -> c.getClasses().size());

        return user;
    }
}
