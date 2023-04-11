package com.tikelespike.nilee.app.views.character;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

import java.util.Objects;
import java.util.Optional;

/**
 * A helper class to check if a character is sane (not null, not deleted, and owned by the current user).
 * Use {@link #ensureSanity(Long)} or {@link #ensureSanity(PlayerCharacter)} to throw an exception
 * if the character is not sane.
 */
public class CharacterSanityChecker extends Div {

    private final PlayerCharacterService characterService;
    private final User currentUser;

    /**
     * Creates a new sanity checker.
     *
     * @param characterService the service to use to check if the character exists
     * @param currentUser the current user to check if the character is owned by them
     */
    public CharacterSanityChecker(PlayerCharacterService characterService, User currentUser) {
        this.characterService = characterService;
        this.currentUser = currentUser;
    }

    /**
     * Check if the character with the given id exists and its owner is equal to the current user
     *
     * @param characterId the id of the character to check
     * @return true iff the character exists and its owner is equal to the current user
     */
    public boolean sanityCheck(Long characterId) {
        if (characterId == null) {
            return false;
        }
        Optional<PlayerCharacterSnapshot> optPC = characterService.get(characterId);
        if (optPC.isEmpty()) {
            return false;
        }
        PlayerCharacter character = PlayerCharacter.createFromSnapshot(optPC.get());
        return character.getOwner() != null
                && character.getOwner().equals(currentUser)
                && Objects.equals(character.getId(), characterId);
    }

    /**
     * Checks if the given character is not null, its id exists in the database and its owner is equal to the current user.
     *
     * @param character the character to check
     * @return true iff the character is not null, its id exists in the database and its owner is equal to the current user
     */
    public boolean sanityCheck(PlayerCharacter character) {
        if (character == null) {
            return false;
        }
        return sanityCheck(character.getId());
    }

    /**
     * Throws an exception if the character with the given id is not sane as in {@link #sanityCheck(Long)}.
     *
     * @param characterId the id of the character to check
     */
    public void ensureSanity(Long characterId) {
        notifyError(sanityCheck(characterId));
    }

    /**
     * Throws an exception if the given character is not sane as in {@link #sanityCheck(PlayerCharacter)}.
     *
     * @param character the character to check
     */
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
