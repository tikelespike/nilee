package com.tikelespike.nilee.app.views.mainmenu;

import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.PlayerCharacterDTO;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.tikelespike.nilee.core.data.service.UserService;
import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.app.views.character.CharacterSanityChecker;
import com.tikelespike.nilee.app.views.character.CharacterSheetView;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@Route(value = "characters", layout = MainLayout.class)
@PermitAll
public class CharacterListView extends VerticalLayout implements HasDynamicTitle {

    private final TextField newCharacterNameTF;

    private final Grid<PlayerCharacter> characterGrid;

    private final UserService userService;
    private final PlayerCharacterService characterService;
    private final CharacterSanityChecker sanityChecker;

    private User currentUser;


    public CharacterListView(AuthenticatedUser authenticatedUser, PlayerCharacterService playerCharacterService,
                             UserService userService) {
        this.currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not " +
            "authenticated"));
        this.userService = userService;
        this.characterService = playerCharacterService;
        this.sanityChecker = new CharacterSanityChecker(characterService, currentUser);


        this.characterGrid = createCharacterGrid();
        updateCharacterGrid();

        this.newCharacterNameTF = new TextField(getTranslation("character_list.new_character.name.label"));
        newCharacterNameTF.addFocusShortcut(Key.KEY_N, KeyModifier.CONTROL, KeyModifier.ALT);
        Button addCharacterButton = createAddCharacterButton();
        var addCharacterLayout = new HorizontalLayout(newCharacterNameTF, addCharacterButton);
        addCharacterLayout.setVerticalComponentAlignment(Alignment.END, addCharacterButton);

        add(addCharacterLayout, characterGrid);
    }

    private Grid<PlayerCharacter> createCharacterGrid() {
        Grid<PlayerCharacter> grid = new Grid<>(PlayerCharacter.class, false);
        grid.addColumn(PlayerCharacter::getName).setHeader(getTranslation("character_list.list.headings.name")).setAutoWidth(true).setFlexGrow(0);
        grid.addColumn(new ComponentRenderer<>(this::createOpenPCButton)).setHeader(getTranslation("character_list.list.headings.open"));
        grid.addColumn(new ComponentRenderer<>(this::createDeletePCButton)).setHeader(getTranslation("character_list.list.headings.delete"));
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
        Button button = new Button(getTranslation("character_list.new_character.button.label"));
        button.addClickShortcut(Key.ENTER);
        button.addClickListener(e -> addPC(new PlayerCharacter(newCharacterNameTF.getValue(), currentUser)));
        return button;
    }

    private void addPC(PlayerCharacter character) {
        characterService.update(character);
        updateUserInfo();
        updateCharacterGrid();
    }

    private Button createOpenPCButton(PlayerCharacter pc) {
        Button openButton = new Button(getTranslation("generic.open"));
        ComponentEventListener<ClickEvent<Button>> navigateToCharacterSheet = e -> openPCSheet(pc);
        openButton.addClickListener(navigateToCharacterSheet);
        return openButton;
    }

    private void openPCSheet(PlayerCharacter pc) {
        sanityChecker.ensureSanity(pc);
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
        confirmDialog.setHeaderTitle(getTranslation("character_list.list.delete.confirm.title", pc.getName()));
        confirmDialog.add(getTranslation("character_list.list.delete.confirm.body", pc.getName()));

        Button confirmButton = new Button(getTranslation("generic.delete"));
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        confirmButton.addClickListener(e -> {
            deletePC(pc);
            confirmDialog.close();
        });

        Button cancelButton = new Button(getTranslation("generic.cancel"));
        cancelButton.addThemeVariants();
        cancelButton.addClickListener(e -> confirmDialog.close());

        confirmDialog.getFooter().add(cancelButton, confirmButton);
        return confirmDialog;
    }

    private void deletePC(PlayerCharacter pc) {
        sanityChecker.ensureSanity(pc);
        characterService.delete(pc.getId());
        updateUserInfo(); // since the user object will now have the character removed too
        updateCharacterGrid();
    }

    private void updateCharacterGrid() {
        characterGrid.setItems(currentUser.getCharacters().stream().map(PlayerCharacterDTO::toBO).toList());
    }


    private void updateUserInfo() {
        currentUser = userService.get(currentUser.getId()).orElseThrow(() -> new IllegalStateException("User not " +
            "found"));
    }

    @Override
    public String getPageTitle() {
        return getTranslation("character_list.title");
    }
}
