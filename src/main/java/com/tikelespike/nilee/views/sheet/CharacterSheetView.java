package com.tikelespike.nilee.views.sheet;

import com.tikelespike.nilee.AppStrings;
import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.tikelespike.nilee.views.mainmenu.CharacterSelectionView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.util.Objects;
import java.util.Optional;

@Route(value = "sheet")
@PermitAll
public class CharacterSheetView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private final PlayerCharacterService characterService;
    private final User currentUser;

    private PlayerCharacter pc;

    public CharacterSheetView(UserService userService, AuthenticatedUser authenticatedUser, PlayerCharacterService characterService) {
        this.characterService = characterService;
        this.currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not authenticated"));

        // initialization happens in setParameter based on the given character
        add(AppStrings.CHARACTER_NOT_FOUND);

        // prevent scrolling down so the header disappears
        getStyle().set("position", "sticky");
        getStyle().set("top", "0");
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        ensureSanity(parameter);
        setPlayerCharacter(characterService.get(parameter).get());
    }

    public void setPlayerCharacter(PlayerCharacter pc) {
        this.pc = pc;
        init();
    }

    private void init() {
        removeAll();
        setPadding(true);
        Component header = createHeader();
        add(header);

        Text placeholderText = new Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.".repeat(20));
        Text placeholderText2 = new Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.".repeat(20));
        Text placeholderText3 = new Text("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.".repeat(20));
        TabSheet tabSheet = new TabSheet();
        tabSheet.add("Tab 1", new Scroller(placeholderText));
        tabSheet.add("Tab 2", new Scroller(placeholderText2));
        tabSheet.add("Tab 3", new Scroller(placeholderText3));
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_EQUAL_WIDTH_TABS);
        add(tabSheet);
    }

    private Component createHeader() {
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);
        // set the style of the header so it always stays at the top of the page
        header.getStyle().set("position", "sticky");
        header.getStyle().set("top", "0");
        header.getStyle().set("z-index", "99");
        header.getStyle().set("background-color", "var(--lumo-base-color)");

        Button backButton = new Button("Back to overview");
        backButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(CharacterSelectionView.class)));

        H3 nameTitle = new H3(pc.getName());
        nameTitle.getElement().getStyle().set("margin-top", "0.5em");

        Button hitPointsButton = new Button(pc.getHitPoints() + "/" + pc.getMaxHitPoints() + " HP");

        header.add(backButton, nameTitle, hitPointsButton);
        return header;
    }

    public PlayerCharacter getPlayerCharacter() {
        return pc;
    }

    private boolean sanityCheck(Long characterId) {
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

    private void ensureSanity(Long characterId) {
        if (!sanityCheck(characterId)) {
            Notification.show(AppStrings.CHARACTER_NOT_FOUND);
            throw new IllegalStateException("Invalid character (null, not found, or not owned by current user)");
        }
    }

    @Override
    public String getPageTitle() {
        return pc == null ? "My Characters" : pc.getName() + " - Character Sheet";
    }
}