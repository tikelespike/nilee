package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.data.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service used to store and obtain {@link User Users} to/from the database.
 */
@Service
public class UserService {

    private final UserRepository repository;

    /**
     * Creates a new UserService accessing the database using the given JPA repository.
     *
     * @param repository the repository used to access the database
     */
    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a user by its unique id.
     *
     * @param id unique numerical identifier of the user
     *
     * @return an optional containing the corresponding user object, or an empty optional if the id could not be found
     *         in the databases
     */
    public Optional<User> get(Long id) {
        return repository.findById(id);
    }

    /**
     * Stores a user in the database.
     *
     * @param entity the user to store
     *
     * @return the saved user
     */
    public User update(User entity) {
        return repository.save(entity);
    }

    /**
     * Deletes a user from the database.
     *
     * @param id the id of the user to delete
     */
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
