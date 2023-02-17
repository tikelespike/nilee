package com.tikelespike.nilee.views.characterselection;

import com.tikelespike.nilee.AppStrings;
import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.tikelespike.nilee.views.about.AboutView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("My Characters")
@Route(value = "characters")
@PermitAll
public class CharacterSelectionView extends VerticalLayout {

    private TextField newCharacterName;
    private Button addCharacterButton;

    private Grid<PlayerCharacter> characterGrid;

    private final User currentUser;


    public CharacterSelectionView(AuthenticatedUser authenticatedUser, PlayerCharacterService playerCharacterService, UserService userService ) {
        if (authenticatedUser.get().isEmpty()) {
            throw new IllegalStateException("User not authenticated");
        }
        currentUser = authenticatedUser.get().get();

        initComponents(playerCharacterService, userService);
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        add("Character Selection");
        add(characterGrid, newCharacterName, addCharacterButton);
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
            updateCharacterGrid();
        });
        characterGrid = new Grid<>(PlayerCharacter.class, false);
        characterGrid.addColumn(PlayerCharacter::getName).setHeader("Name");
        characterGrid.addColumn(new ComponentRenderer<>(this::createOpenCharacterButton)).setHeader(AppStrings.OPEN_CHARACTER_HEADER);
        updateCharacterGrid();
    }

    private Button createOpenCharacterButton(PlayerCharacter pc) {
        Button openButton = new Button(AppStrings.OPEN_CHARACTER_CTA);
        ComponentEventListener<ClickEvent<Button>> navigateToCharacterSheet = e -> {
            openButton.getUI().ifPresent(ui -> ui.navigate(AboutView.class).ifPresent(aboutView -> {
                aboutView.setPlayerCharacter(pc);
            }));
        };
        openButton.addClickListener(navigateToCharacterSheet);
        return openButton;
    }

    private void updateCharacterGrid() {
        characterGrid.setItems(currentUser.getCharacters());
    }
}
