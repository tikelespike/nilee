package com.tikelespike.nilee.app.views.character;

import com.tikelespike.nilee.core.data.entity.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

import java.util.Objects;
import java.util.Optional;

public class CharacterSanityChecker extends Div {

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

    public boolean sanityCheck(PlayerCharacter character) {
        if (character == null) {
            return false;
        }
        return sanityCheck(character.getId());
    }

    public void ensureSanity(Long characterId) {
        notifyError(sanityCheck(characterId));
    }

    public void ensureSanity(PlayerCharacter character) {
        notifyError(sanityCheck(character));
    }

    private void notifyError(boolean isCharacterSane) {
        if (!isCharacterSane) {
            Notification.show(getTranslation("error.character_not_found"));
            throw new IllegalStateException("Invalid character (null, not found, or not owned by current user)");
        }
    }
}
