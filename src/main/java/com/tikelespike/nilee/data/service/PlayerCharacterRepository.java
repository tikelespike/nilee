package com.tikelespike.nilee.data.service;

import com.tikelespike.nilee.data.entity.PlayerCharacter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PlayerCharacterRepository extends JpaRepository<PlayerCharacter, Long>, JpaSpecificationExecutor<PlayerCharacter> {

}
