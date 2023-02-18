package com.tikelespike.nilee.views.sheet;

import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.util.Optional;

@Route(value = "sheet")
@PermitAll
public class CharacterSheetView extends HorizontalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private final Label characterNameLabel;

    private final PlayerCharacterService characterService;
    private final User currentUser;

    private PlayerCharacter pc;

    public CharacterSheetView(UserService userService, AuthenticatedUser authenticatedUser, PlayerCharacterService characterService) {
        this.characterService = characterService;
        this.currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not authenticated"));
        characterNameLabel = new Label("No character selected");

        setMargin(true);

        add(characterNameLabel);
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        if (parameter == null) {
            Notification.show("No character selected");
            return;
        }

        Optional<PlayerCharacter> optPC = characterService.get(parameter);
        if (optPC.isEmpty() || !optPC.get().getOwner().equals(currentUser)) {
            Notification.show("Character not found");
            return;
        }

        setPlayerCharacter(optPC.get());
    }

    public void setPlayerCharacter(PlayerCharacter pc) {
        this.pc = pc;
        characterNameLabel.setText(pc.getName());
    }

    public PlayerCharacter getPlayerCharacter() {
        return pc;
    }

    @Override
    public String getPageTitle() {
        return pc == null ? "My Characters" : pc.getName() + " - Character Sheet";
    }
}
