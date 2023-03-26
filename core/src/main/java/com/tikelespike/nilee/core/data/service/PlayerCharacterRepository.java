package com.tikelespike.nilee.core.data.service;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.PlayerCharacterDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacterDTO, Long>, JpaSpecificationExecutor<PlayerCharacterDTO> {

}
