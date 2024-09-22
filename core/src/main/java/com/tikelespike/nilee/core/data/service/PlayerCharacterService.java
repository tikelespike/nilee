package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service used to store and obtain player character snapshots to/from the database.
 */
@Service
public class PlayerCharacterService {

    private final PlayerCharacterRepository repository;

    /**
     * Creates a new PlayerCharacterService accessing the database using the given JPA repository.
     *
     * @param repository the repository used to access the database
     */
    public PlayerCharacterService(PlayerCharacterRepository repository) {
        this.repository = repository;
    }

    /**
     * Retrieves a player character snapshot by its unique id.
     *
     * @param id unique numerical identifier of the snapshot
     *
     * @return an optional containing the corresponding snapshot object, or an empty optional if the id could not be
     *         found in the databases
     */
    @Transactional
    public Optional<PlayerCharacterSnapshot> get(Long id) {
        return repository.findById(id);
    }

    /**
     * Updates a player character snapshot. If there exists a newer version of this snapshot in the database, the newer
     * version will not be overridden and this method has no effect.
     *
     * @param playerCharacterSnapshot the snapshot to store
     *
     * @return the saved snapshot
     */
    @Transactional
    public PlayerCharacterSnapshot update(PlayerCharacterSnapshot playerCharacterSnapshot) {
        return repository.save(playerCharacterSnapshot);
    }

    /**
     * Updates a player character snapshot. If the force flag is set to true, this will override newer versions of the
     * same snapshot that are already in the database.
     *
     * @param playerCharacterSnapshot the snapshot to store
     * @param force if true, newer versions already in the database will be overridden. Otherwise, this method
     *         has no effect if there is a newer snapshot present in the database.
     *
     * @return the saved snapshot
     */
    @Transactional
    public PlayerCharacterSnapshot update(PlayerCharacterSnapshot playerCharacterSnapshot, boolean force) {
        if (force) {
            // Force update by overriding the version, ignoring all changes made between loading the given dto and this
            // save operation.
            repository.findById(playerCharacterSnapshot.getId())
                    .ifPresent(existing -> playerCharacterSnapshot.setVersion(existing.getVersion()));
        }
        return repository.save(playerCharacterSnapshot);
    }

    /**
     * Deletes a snapshot from the database by its id.
     *
     * @param id the unique numeric identifier of the snapshot to delete
     */
    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
