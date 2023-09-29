package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.components.BarComponent;
import com.tikelespike.nilee.app.components.HeaderComponent;
import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.app.views.character.CharacterSanityChecker;
import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.app.views.character.editor.CharacterEditorView;
import com.tikelespike.nilee.app.views.character.sheet.dice.RollAnimator;
import com.tikelespike.nilee.app.views.mainmenu.CharacterListView;
import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.tikelespike.nilee.core.game.GameSession;
import com.tikelespike.nilee.core.game.GameSessionManager;
import com.tikelespike.nilee.core.game.RollBus;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;
import java.util.UUID;

@Route(value = "sheet")
@PermitAll
public class CharacterSheetView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private final PlayerCharacterService characterService;

    private final CharacterSanityChecker sanityChecker;
    private final TranslationProvider translationProvider;

    private final User currentUser;

    private GameSession gameSession = null;

    private PlayerCharacter pc;
    private CharacterSaver characterSaver;

    public CharacterSheetView(AuthenticatedUser authenticatedUser,
                              PlayerCharacterService characterService,
                              TranslationProvider translationProvider) {
        this.characterService = characterService;
        this.currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not " +
                "authenticated"));
        this.sanityChecker = new CharacterSanityChecker(characterService, currentUser);
        this.translationProvider = translationProvider;
        this.gameSession = GameSessionManager.getInstance().newSession();

        // initialization happens in setParameter based on the given character
        add(getTranslation("error.character_not_found"));

        // prevent scrolling down so the header disappears
        getStyle().set("position", "sticky");
        getStyle().set("top", "0");
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        sanityChecker.ensureSanity(parameter);
        initWithCharacter(PlayerCharacter.createFromSnapshot(characterService.get(parameter).get()));
    }

    private void initWithCharacter(PlayerCharacter pc) {
        this.pc = pc;
        this.characterSaver = new CharacterSaver(pc, characterService, sanityChecker);
        removeAll();
        setPadding(true);
        Component header = createHeader();
        add(header);

        TabSheet tabSheet = createTabSheet();
        add(tabSheet);

        Notification.show("Session: " + gameSession.getId().toString(), 8000, Notification.Position.BOTTOM_START);
    }

    private TabSheet createTabSheet() {
        Text placeholderText =
                new Text(
                        ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " + "tempor incididunt" +
                                " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " + "ullamco " +
                                "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
                                "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.").repeat(
                                20));
        Text placeholderText2 =
                new Text(
                        ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " + "tempor incididunt" +
                                " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " + "ullamco " +
                                "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
                                "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.").repeat(
                                20));
        Text placeholderText3 =
                new Text(
                        ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " + "tempor incididunt" +
                                " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " + "ullamco " +
                                "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
                                "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non " + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.").repeat(
                                20));
        RollBus rollBus = gameSession.getRollBus();
        RollAnimator rollAnimator = new RollAnimator(translationProvider, rollBus);
        add(rollAnimator);

        Component abilities = new AbilitiesView(rollBus, translationProvider, pc);

        Scroller scroller = new Scroller(abilities);
        Tab tab = new Tab("Tab 1");
        TabSheet tabSheet = new TabSheet();
        tabSheet.add(tab, scroller);
        tabSheet.add("Tab 2", new Scroller(placeholderText2));
        tabSheet.add("Tab 3", new Scroller(placeholderText3));
        tabSheet.addThemeVariants(TabSheetVariant.LUMO_TABS_EQUAL_WIDTH_TABS);
        tabSheet.setWidthFull();
        return tabSheet;
    }

    private Component createHeader() {
        BarComponent header = new HeaderComponent();

        Button backButton = new Button(getTranslation("character_sheet.header.back"));
        backButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(CharacterListView.class)));

        Button editButton = new Button(getTranslation("character_sheet.header.edit"));
        editButton.addClickListener(e -> editPC());

        Button sessionButton = new Button("Session");
        sessionButton.addClickListener(e -> {
            Dialog dialog = new Dialog();
            dialog.add(new Text("Session"));
            TextField sessionIDField = new TextField("Session ID");
            dialog.add(sessionIDField);
            Button joinButton = new Button("Join");
            joinButton.addClickListener(e1 -> {
                UUID sessionID = UUID.fromString(sessionIDField.getValue());
                this.gameSession = GameSessionManager.getInstance().getSession(sessionID).get();
                dialog.close();
                initWithCharacter(this.pc);
            });
            dialog.add(joinButton);
            dialog.open();
        });
        sessionButton.setText("Session " + gameSession.getId().toString());

        H3 nameTitle = new H3(pc.getName());
        nameTitle.getElement().getStyle().set("margin-top", "0.5em");

        HitPointsDisplay hpDisplay = new HitPointsDisplay(pc.getHitPoints(), characterSaver);

        header.addLeft(backButton, editButton, sessionButton);
        header.addCenter(nameTitle);
        header.addRight(hpDisplay);
        return header;
    }

    private void editPC() {
        sanityChecker.ensureSanity(pc.getId());
        getUI().ifPresent(ui -> ui.navigate(CharacterEditorView.class, pc.getId()));
    }

    @Override
    public String getPageTitle() {
        return pc == null ? "My Characters" : getTranslation("character_sheet.title", pc.getName());
    }
}