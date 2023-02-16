package com.tikelespike.nilee.data.service;

import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
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

    public Optional<PlayerCharacter> get(Long id) {
        return repository.findById(id);
    }

    public PlayerCharacter update(PlayerCharacter entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Page<PlayerCharacter> list(Pageable pageable) {
        return repository.findAll(pageable);
    }
}
