package com.tikelespike.nilee.app.views.character;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.PlayerCharacterSnapshot;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import org.springframework.dao.OptimisticLockingFailureException;

import java.util.function.Consumer;

public class CharacterSaver extends Div {

    public enum SaveResult {
        SAVED,
        CANCELLED,
        CHANGES_DISCARDED
    }

    private PlayerCharacterService characterService;

    private PlayerCharacter playerCharacter;
    private CharacterSanityChecker sanityChecker;


    public CharacterSaver(PlayerCharacter playerCharacter, PlayerCharacterService characterService, User currentUser) {
        this.playerCharacter = playerCharacter;
        this.characterService = characterService;
        this.sanityChecker = new CharacterSanityChecker(characterService, currentUser);
    }

    public void save() {
        save(r -> {});
    }

    public void save(Consumer<SaveResult> andThen) {
        sanityChecker.ensureSanity(playerCharacter.getId());
        try {
            characterService.update(playerCharacter.createSnapshot());
        } catch (OptimisticLockingFailureException e) {
            showSaveWarningDialog(andThen);
            return;
        }
        playerCharacter.restoreSnapshot(characterService.get(playerCharacter.getId()).get());
        andThen.accept(SaveResult.SAVED);
    }

    private void showSaveWarningDialog(Consumer<SaveResult> andThen) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setHeader(getTranslation("character_editor.error.save_conflict.header"));
        dialog.setText(getTranslation("character_editor.error.save_conflict.body"));
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
        dialog.setRejectText(getTranslation("character_editor.error.save_conflict.discard"));
        dialog.open();
    }

    public void discard() {
        PlayerCharacterSnapshot snapshot = characterService.get(playerCharacter.getId()).get();
        playerCharacter.restoreSnapshot(snapshot);
    }
}
