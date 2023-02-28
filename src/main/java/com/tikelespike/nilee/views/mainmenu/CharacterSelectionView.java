package com.tikelespike.nilee.views.mainmenu;

import com.tikelespike.nilee.AppStrings;
import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.tikelespike.nilee.views.character.CharacterSheetView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@PageTitle("My Characters")
@Route(value = "characters", layout = MainLayout.class)
@PermitAll
public class CharacterSelectionView extends VerticalLayout {

    private final TextField newCharacterNameTF;

    private final Grid<PlayerCharacter> characterGrid;

    private final UserService userService;
    private final PlayerCharacterService characterService;
    private User currentUser;


    public CharacterSelectionView(AuthenticatedUser authenticatedUser, PlayerCharacterService playerCharacterService, UserService userService) {
        this.currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not authenticated"));
        this.userService = userService;
        this.characterService = playerCharacterService;

        this.characterGrid = createCharacterGrid();
        updateCharacterGrid();

        this.newCharacterNameTF = new TextField(AppStrings.CHARACTER_NAME_HEADER);
        newCharacterNameTF.addFocusShortcut(Key.KEY_N, KeyModifier.CONTROL, KeyModifier.ALT);
        Button addCharacterButton = createAddCharacterButton();
        var addCharacterLayout = new HorizontalLayout(newCharacterNameTF, addCharacterButton);
        addCharacterLayout.setVerticalComponentAlignment(Alignment.END, addCharacterButton);

        add(addCharacterLayout, characterGrid);
    }

    private Grid<PlayerCharacter> createCharacterGrid() {
        Grid<PlayerCharacter> grid = new Grid<>(PlayerCharacter.class, false);
        grid.addColumn(PlayerCharacter::getName).setHeader(AppStrings.CHARACTER_NAME_HEADER).setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createOpenPCButton)).setHeader(AppStrings.OPEN_CHARACTER_HEADER);
        grid.addColumn(new ComponentRenderer<>(this::createDeletePCButton)).setHeader(AppStrings.DEL_CHARACTER_HEADER);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addSelectionListener(e -> {
            if (e.getFirstSelectedItem().isPresent()) {
                PlayerCharacter pc = e.getFirstSelectedItem().get();
                openPCSheet(pc);
            }
        });
        return grid;
    }

    private Button createAddCharacterButton() {
        Button button = new Button(AppStrings.ADD_CHARACTER_CTA);
        button.addClickShortcut(Key.ENTER);
        button.addClickListener(e -> addPC(createNewCharacter(newCharacterNameTF.getValue())));
        return button;
    }

    private PlayerCharacter createNewCharacter(String name) {
        PlayerCharacter newCharacter = new PlayerCharacter();
        newCharacter.setName(name);
        newCharacter.setDex(10);
        newCharacter.setOwner(currentUser);
        return newCharacter;
    }

    private void addPC(PlayerCharacter character) {
        characterService.update(character);
        updateUserInfo();
        updateCharacterGrid();
    }

    private Button createOpenPCButton(PlayerCharacter pc) {
        Button openButton = new Button(AppStrings.OPEN_CHARACTER_CTA);
        ComponentEventListener<ClickEvent<Button>> navigateToCharacterSheet = e -> openPCSheet(pc);
        openButton.addClickListener(navigateToCharacterSheet);
        return openButton;
    }

    private void openPCSheet(PlayerCharacter pc) {
        ensureSanity(pc);
        getUI().ifPresent(ui -> ui.navigate(CharacterSheetView.class, pc.getId()));
    }

    private Button createDeletePCButton(PlayerCharacter pc) {
        Button deleteButton = new Button();
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_TERTIARY);
        deleteButton.setIcon(new Icon(VaadinIcon.TRASH));

        Dialog confirmDialog = createConfirmDialog(pc);
        deleteButton.addClickListener(e -> confirmDialog.open());

        return deleteButton;
    }

    private Dialog createConfirmDialog(PlayerCharacter pc) {
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle(AppStrings.DEL_CHARACTER_HEADER);
        confirmDialog.add(String.format(AppStrings.DEL_CHARACTER_CONFIRMATION_BODY, pc.getName()));

        Button confirmButton = new Button(AppStrings.DEL_CHARACTER_CTA);
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        confirmButton.addClickListener(e -> {
            deletePC(pc);
            confirmDialog.close();
        });

        Button cancelButton = new Button(AppStrings.CANCEL_CTA);
        cancelButton.addThemeVariants();
        cancelButton.addClickListener(e -> confirmDialog.close());

        confirmDialog.getFooter().add(cancelButton, confirmButton);
        return confirmDialog;
    }

    private void deletePC(PlayerCharacter pc) {
        ensureSanity(pc);
        characterService.delete(pc.getId());
        updateUserInfo(); // since the user object will now have the character removed too
        updateCharacterGrid();
    }

    private void updateCharacterGrid() {
        characterGrid.setItems(currentUser.getCharacters());
    }

    private boolean sanityCheck(PlayerCharacter character) {
        return character != null
            && character.getOwner() != null
            && character.getOwner().equals(currentUser)
            && character.getId() != null
            && characterService.get(character.getId()).isPresent();
    }

    private void ensureSanity(PlayerCharacter character) {
        if (!sanityCheck(character)) {
            Notification.show(AppStrings.CHARACTER_NOT_FOUND);
            throw new IllegalStateException("Invalid character (null, not found, or not owned by current user)");
        }
    }

    private void updateUserInfo() {
        currentUser = userService.get(currentUser.getId()).orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
