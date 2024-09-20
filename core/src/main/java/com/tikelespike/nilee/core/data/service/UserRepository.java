package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.data.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * JPA repository for users.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * Retrieve a user from the database from its unique username.
     *
     * @param username unique username of that user
     *
     * @return the user with that username, loaded from the database, or null if that username does not exist.
     */
    User findByUsername(String username);
}
