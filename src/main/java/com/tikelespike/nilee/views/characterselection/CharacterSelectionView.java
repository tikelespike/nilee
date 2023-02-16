package com.tikelespike.nilee.views.characterselection;

import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("My Characters")
@Route(value = "characters")
@PermitAll
public class CharacterSelectionView extends VerticalLayout {

    private TextField newCharacterName;
    private Button addCharacterButton;

    private ListBox<String> characterListBox;

    private User currentUser;


    public CharacterSelectionView(AuthenticatedUser authenticatedUser, PlayerCharacterService playerCharacterService, UserService userService ) {
        currentUser = authenticatedUser.get().get();

        initComponents(playerCharacterService, userService);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add("Character Selection");
        add(characterListBox, newCharacterName, addCharacterButton);
    }

    private void initComponents(PlayerCharacterService playerCharacterService, UserService userService) {
        newCharacterName = new TextField("Name");
        addCharacterButton = new Button("Add new Character");
        addCharacterButton.addClickListener(e -> {
            PlayerCharacter newCharacter = new PlayerCharacter();
            newCharacter.setName(newCharacterName.getValue());
            newCharacter.setDex(10);
            newCharacter.setOwner(currentUser);
            currentUser.getCharacters().add(newCharacter);
            playerCharacterService.update(newCharacter);
            userService.update(currentUser);
            updateListBox();
        });
        characterListBox = new ListBox<>();
        updateListBox();
    }

    private void updateListBox() {
        characterListBox.setItems(currentUser.getCharacters().stream().map(PlayerCharacter::getName));
    }
}
