package com.tikelespike.nilee.views.about;

import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.tikelespike.nilee.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import java.util.Optional;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends HorizontalLayout implements HasUrlParameter<Long> {

    private final Label characterNameLabel;

    private final PlayerCharacterService characterService;
    private final User currentUser;

    private PlayerCharacter pc;

    public AboutView(UserService userService, AuthenticatedUser authenticatedUser, PlayerCharacterService characterService) {
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
}
