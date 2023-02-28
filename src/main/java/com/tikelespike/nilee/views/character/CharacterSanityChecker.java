package com.tikelespike.nilee.views.character;

import com.tikelespike.nilee.AppStrings;
import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.vaadin.flow.component.notification.Notification;

import java.util.Objects;
import java.util.Optional;

public class CharacterSanityChecker {

    private final PlayerCharacterService characterService;
    private final User currentUser;

    public CharacterSanityChecker(PlayerCharacterService characterService, User currentUser) {
        this.characterService = characterService;
        this.currentUser = currentUser;
    }

    public boolean sanityCheck(Long characterId) {
        if (characterId == null) {
            return false;
        }
        Optional<PlayerCharacter> optPC = characterService.get(characterId);
        if (optPC.isEmpty()) {
            return false;
        }
        PlayerCharacter character = optPC.get();
        return character.getOwner() != null
            && character.getOwner().equals(currentUser)
            && Objects.equals(character.getId(), characterId);
    }

    public void ensureSanity(Long characterId) {
        if (!sanityCheck(characterId)) {
            Notification.show(AppStrings.CHARACTER_NOT_FOUND);
            throw new IllegalStateException("Invalid character (null, not found, or not owned by current user)");
        }
    }
}
