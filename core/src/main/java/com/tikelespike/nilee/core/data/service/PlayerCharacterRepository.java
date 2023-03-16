package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.data.entity.character.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long>, JpaSpecificationExecutor<PlayerCharacter> {

}
