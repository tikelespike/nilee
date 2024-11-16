package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import com.tikelespike.nilee.core.character.classes.UnifiedClassInstanceMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service used to store and obtain player character snapshots to/from the database.
 */
@Service
public class PlayerCharacterService {

    @Autowired // Necessary to avoid circular dependency
    @Lazy
    private PlayerCharacterService selfProxy;
    private final PlayerCharacterRepository repository;
    private final UnifiedClassInstanceMapper classMapper;

    /**
     * Creates a new PlayerCharacterService accessing the database using the given JPA repository.
     *
     * @param repository the repository used to access the database
     * @param classMapper the mapper used for converting between class instances and their database entity
     *         representations
     */
    public PlayerCharacterService(PlayerCharacterRepository repository, UnifiedClassInstanceMapper classMapper) {
        this.repository = repository;
        this.classMapper = classMapper;
    }

    /**
     * Retrieves a player character by its unique id. This is a convenience method for retrieving a player character
     * snapshot and creating a player character business object from it.
     *
     * @param id the unique numerical identifier of the player character to retrieve
     *
     * @return an optional containing the corresponding player character, or an empty optional if the id could not be
     *         found in the database
     */
    @Transactional
    public Optional<PlayerCharacter> getCharacter(Long id) {
        return selfProxy.get(id).map(snapshot -> PlayerCharacter.createFromSnapshot(snapshot, classMapper));
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
        Optional<PlayerCharacterSnapshot> optionalSnapshot = repository.findById(id);
        optionalSnapshot.ifPresent(s -> s.getClasses().size()); // initialize classes association eagerly
        return optionalSnapshot;
    }

    /**
     * Updates a player character. This is a convenience method for creating a snapshot from the given player character
     * and updating the snapshot in the database.
     *
     * @param playerCharacter the player character to store
     * @param force if true, newer versions already in the database will be overridden. Otherwise, this method
     *         has no effect if there is a newer snapshot present in the database.
     *
     * @return the updated player character as it is now stored in the database
     */
    @Transactional
    public PlayerCharacter update(PlayerCharacter playerCharacter, boolean force) {
        return PlayerCharacter.createFromSnapshot(selfProxy.update(playerCharacter.createSnapshot(), force),
                classMapper);
    }

    /**
     * Updates a player character. This is a convenience method for creating a snapshot from the given player character
     * and updating the snapshot in the database. If there exists a newer version of this snapshot in the database, the
     * newer version will not be overridden and this method has no effect.
     *
     * @param playerCharacter the player character to store
     *
     * @return the updated player character as it is now stored in the database
     */
    @Transactional
    public PlayerCharacter update(PlayerCharacter playerCharacter) {
        return selfProxy.update(playerCharacter, false);
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
        return selfProxy.update(playerCharacterSnapshot, false);
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
        PlayerCharacterSnapshot savedSnapshot = repository.save(playerCharacterSnapshot);
        savedSnapshot.getClasses().size(); // initialize classes association eagerly to avoid issues with lazy loading
        return savedSnapshot;
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
