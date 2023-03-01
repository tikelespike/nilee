package com.tikelespike.nilee.views.character;

import com.tikelespike.nilee.AppStrings;
import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.PlayerCharacterService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.tikelespike.nilee.views.character.editor.AbilitiesEditorView;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

import javax.annotation.security.PermitAll;

@Route(value = "editor")
@PermitAll
public class CharacterEditorView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private final PlayerCharacterService characterService;
    private final CharacterSanityChecker sanityChecker;
    private Accordion accordion;
    private PlayerCharacter pc;
    private AbilitiesEditorView abilitiesEditorView;


    public CharacterEditorView(PlayerCharacterService characterService, AuthenticatedUser authenticatedUser) {
        this.characterService = characterService;
        User currentUser = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not " +
            "authenticated"));
        this.sanityChecker = new CharacterSanityChecker(characterService, currentUser);
        add(AppStrings.CHARACTER_NOT_FOUND);
    }

    private void init() {
        removeAll();
        setAlignItems(Alignment.CENTER);

        add(new H1("Character Creation"));

        accordion = createAccordion();
        add(accordion);

        Button saveButton = new Button("Save", e -> save());
        add(saveButton);
    }

    private Accordion createAccordion() {
        Accordion accordion = new Accordion();
        accordion.add("Description", new Span(pc.getName()));
        this.abilitiesEditorView = new AbilitiesEditorView(pc.getSdciwc());
        accordion.add("Abilities & Stats", abilitiesEditorView);
        accordion.addOpenedChangeListener(e -> {
            abilitiesEditorView.update();
        });
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
        this.pc = characterService.get(parameter).get();
        init();
    }

    private void save() {
        sanityChecker.ensureSanity(pc.getId());
        abilitiesEditorView.update();
        characterService.update(pc);
        getUI().ifPresent(ui -> ui.navigate(CharacterSheetView.class, pc.getId()));
    }
}
