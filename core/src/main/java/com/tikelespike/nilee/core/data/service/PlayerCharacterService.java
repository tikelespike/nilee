package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PlayerCharacterService {

    private final PlayerCharacterRepository repository;

    public PlayerCharacterService(PlayerCharacterRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Optional<PlayerCharacterSnapshot> get(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public PlayerCharacterSnapshot update(PlayerCharacterSnapshot playerCharacterSnapshot) {
        return update(playerCharacterSnapshot, false);
    }

    @Transactional
    public PlayerCharacterSnapshot update(PlayerCharacterSnapshot playerCharacterSnapshot, boolean force) {
        if (force) {
            // Force update by overriding the version, ignoring all changes made between loading the given dto and this
            // save operation.
            playerCharacterSnapshot.setVersion(repository.findById(playerCharacterSnapshot.getId()).get().getVersion());
        }
        return repository.save(playerCharacterSnapshot);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public Page<PlayerCharacterSnapshot> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
