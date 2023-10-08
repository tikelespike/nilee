package com.tikelespike.nilee.app.views.character.editor;

import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.app.views.character.CharacterSanityChecker;
import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.app.views.character.sheet.CharacterSheetView;
import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@Route(value = "editor")
@PermitAll
public class CharacterEditorView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private final PlayerCharacterService characterService;
    private final CharacterSanityChecker sanityChecker;
    private final User currentUser;
    private CharacterSaver characterSaver;
    private Accordion accordion;
    private PlayerCharacter pc;
    private AbilitiesEditorView abilitiesEditorView;


    public CharacterEditorView(PlayerCharacterService characterService, AuthenticatedUser authenticatedUser) {
        this.characterService = characterService;
        this.currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not " +
                "authenticated"));
        this.sanityChecker = new CharacterSanityChecker(characterService, currentUser);
        add(getTranslation("error.character_not_found"));
    }

    private void init() {
        removeAll();
        setAlignItems(Alignment.CENTER);

        add(new H1(getTranslation("character_editor.title")));

        accordion = createAccordion();
        add(accordion);

        Button saveButton = new Button(getTranslation("generic.save"), e -> save());
        add(saveButton);
    }

    private Accordion createAccordion() {
        Accordion accordion = new Accordion();
        accordion.add("Description", new Span(pc.getName()));
        this.abilitiesEditorView = new AbilitiesEditorView(pc);
        accordion.add(getTranslation("character_editor.abilities.title"), abilitiesEditorView);
        accordion.addOpenedChangeListener(e -> abilitiesEditorView.update());
        accordion.add("Class", new Span("Class"));
        return accordion;
    }

    @Override
    public String getPageTitle() {
        return "Character Editor";
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        sanityChecker.ensureSanity(parameter);
        this.pc = PlayerCharacter.createFromSnapshot(characterService.get(parameter).get());
        this.characterSaver = new CharacterSaver(pc, characterService, sanityChecker);
        init();
    }

    private void save() {
        abilitiesEditorView.update();
        characterSaver.save(r -> {
            if (r == CharacterSaver.SaveResult.SAVED || r == CharacterSaver.SaveResult.CHANGES_DISCARDED) {
                getUI().ifPresent(ui -> ui.navigate(CharacterSheetView.class, pc.getId()));
            }
        });
    }
}
