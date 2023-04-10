package com.tikelespike.nilee.app.views.character;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import org.springframework.dao.OptimisticLockingFailureException;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * A helper class to persist a character to the database. It handles saving (which will write the character to the
 * database and reload the given character to ensure it matches the database) and discarding (which will reload the
 * character with the data present in the database). Conflicts (where the character was changed in the database since
 * the last time it was loaded) are handled by showing a dialog to the user, who can then choose to save the changes
 * anyway, discard the changes or cancel the operation.
 */
public class CharacterSaver extends Div {

    /**
     * The result of a save operation. Describes whether the save operation was successful, cancelled or the changes
     * were discarded.
     */
    public enum SaveResult {
        /**
         * The character was saved successfully.
         */
        SAVED,

        /**
         * The save operation was cancelled by the user (usually by clicking the cancel button in the after a conflict).
         * The character was not saved.
         */
        CANCELLED,

        /**
         * The changes were discarded by the user, either because of clicking the discard button after a conflict
         * or because they were discarded programmatically due to a call of {@link #discard()}.
         * The character was not saved and has been reloaded from the database.
         */
        CHANGES_DISCARDED
    }

    private final PlayerCharacterService characterService;

    private final PlayerCharacter playerCharacter;
    private final CharacterSanityChecker sanityChecker;


    /**
     * Creates a new saver for the given character.
     *
     * @param playerCharacter the character to save and discard
     * @param characterService the service to use to save and load the character
     * @param sanityChecker    the sanity checker to use to check if the character is sane upon saving
     */
    public CharacterSaver(@NotNull PlayerCharacter playerCharacter, @NotNull PlayerCharacterService characterService, @NotNull CharacterSanityChecker sanityChecker) {
        Objects.requireNonNull(playerCharacter);
        Objects.requireNonNull(characterService);
        Objects.requireNonNull(sanityChecker);

        this.playerCharacter = playerCharacter;
        this.characterService = characterService;
        this.sanityChecker = sanityChecker;
    }

    /**
     * Saves the character. If the character was changed in the database since it was last loaded, a dialog will be
     * shown to the user to ask what to do. If the character was not changed in the database, it will be saved
     * immediately.
     */
    public void save() {
        save(r -> {});
    }

    /**
     * Saves the character. If the character was changed in the database since it was last loaded, a dialog will be
     * shown to the user to ask what to do. If the character was not changed in the database, it will be saved
     * immediately. The given consumer acts as a callback after the save operation has completed (successfully or not).
     * For example, upon a saving conflict, it will be called with {@link SaveResult#CANCELLED} if the user clicked
     * the cancel button in the conflict dialog.
     *
     * @param andThen the callback to call after the save operation has completed
     */
    public void save(Consumer<SaveResult> andThen) {
        sanityChecker.ensureSanity(playerCharacter);
        Consumer<SaveResult> nullSafeAndThen = andThen == null ? r -> {} : andThen;

        sanityChecker.ensureSanity(playerCharacter.getId());
        try {
            characterService.update(playerCharacter.createSnapshot());
        } catch (OptimisticLockingFailureException e) {
            showSaveWarningDialog(nullSafeAndThen);
            return;
        }
        Optional<PlayerCharacterSnapshot> optSnapshot = characterService.get(playerCharacter.getId());
        if (optSnapshot.isEmpty()) {
            throw new AssertionError("Character was not found in the database after saving");
        }
        playerCharacter.restoreSnapshot(optSnapshot.get());
        nullSafeAndThen.accept(SaveResult.SAVED);
    }

    private void showSaveWarningDialog(Consumer<SaveResult> andThen) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(getTranslation("error.save_conflict.header"));
        dialog.setText(getTranslation("error.save_conflict.body"));
        dialog.setConfirmText(getTranslation("generic.save"));
        dialog.setCancelable(true);
        dialog.setCancelText(getTranslation("generic.cancel"));
        dialog.addCancelListener(event -> andThen.accept(SaveResult.CANCELLED));
        dialog.addConfirmListener(event -> {
            characterService.update(playerCharacter.createSnapshot(), true);
            playerCharacter.restoreSnapshot(characterService.get(playerCharacter.getId()).get());
            andThen.accept(SaveResult.SAVED);
        });
        dialog.setRejectable(true);
        dialog.addRejectListener(event -> {
            discard();
            andThen.accept(SaveResult.CHANGES_DISCARDED);
        });
        dialog.setRejectText(getTranslation("error.save_conflict.discard"));
        dialog.open();
    }

    /**
     * Discards the changes made to the character since it was last loaded (saving and discarding both reloads the
     * character). The character will be reloaded from the database by restoring the latest available snapshot.
     */
    public void discard() {
        sanityChecker.ensureSanity(playerCharacter);
        PlayerCharacterSnapshot snapshot = characterService.get(playerCharacter.getId()).get();
        playerCharacter.restoreSnapshot(snapshot);
    }
}
