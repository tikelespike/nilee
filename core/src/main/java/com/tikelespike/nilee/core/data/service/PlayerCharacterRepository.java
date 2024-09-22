package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * JPA repository for player character snapshots.
 */
public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacterSnapshot, Long>,
        JpaSpecificationExecutor<PlayerCharacterSnapshot> {

}
