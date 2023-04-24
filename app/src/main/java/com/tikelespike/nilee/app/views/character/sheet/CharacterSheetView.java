package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.components.HeaderComponent;
import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.app.views.character.CharacterSanityChecker;
import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.app.views.character.editor.CharacterEditorView;
import com.tikelespike.nilee.app.views.character.sheet.dice.RollManager;
import com.tikelespike.nilee.app.views.mainmenu.CharacterListView;
import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.stats.ability.AbilityScore;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.router.*;

import javax.annotation.security.PermitAll;

@Route(value = "sheet")
@PermitAll
public class CharacterSheetView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private final PlayerCharacterService characterService;

    private final CharacterSanityChecker sanityChecker;
    private final TranslationProvider translationProvider;

    private final User currentUser;

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
        HorizontalLayout layout = new HorizontalLayout();
        RollManager rollManager = new RollManager(translationProvider);
        for (AbilityScore score : pc.getAbilityScores().getAll()) {
            layout.add(new AbilityScoreBox(score, translationProvider, rollManager));
        }
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        Scroller scroller = new Scroller(layout);
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
        HeaderComponent header = new HeaderComponent();

        Button backButton = new Button(getTranslation("character_sheet.header.back"));
        backButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(CharacterListView.class)));

        H3 nameTitle = new H3(pc.getName());
        nameTitle.getElement().getStyle().set("margin-top", "0.5em");

        Button editButton = new Button(getTranslation("character_sheet.header.edit"));
        editButton.addClickListener(e -> editPC());

        HitPointsDisplay hpDisplay = new HitPointsDisplay(pc.getHitPoints(), characterSaver);

        header.addLeft(backButton, editButton);
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