package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.PlayerCharacterDTO;
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
    public Optional<PlayerCharacterDTO> get(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public PlayerCharacterDTO update(PlayerCharacterDTO playerCharacterDTO) {
        return update(playerCharacterDTO, false);
    }

    @Transactional
    public PlayerCharacterDTO update(PlayerCharacterDTO playerCharacterDTO, boolean force) {
        if (force) {
            // Force update by overriding the version, ignoring all changes made between loading the given dto and this
            // save operation.
            playerCharacterDTO.setVersion(repository.findById(playerCharacterDTO.getId()).get().getVersion());
        }
        return repository.save(playerCharacterDTO);
    }

    @Transactional
    public void delete(Long id) {
        repository.deleteById(id);
    }

    @Transactional
    public Page<PlayerCharacter> list(Pageable pageable) {
        return repository.findAll(pageable).map(PlayerCharacterDTO::toBO);
    }
}
