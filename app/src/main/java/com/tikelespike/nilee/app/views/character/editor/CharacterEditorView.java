package com.tikelespike.nilee.app.views.character.editor;

import com.tikelespike.nilee.app.ClassManagerWrapper;
import com.tikelespike.nilee.app.i18n.UserBasedTranslationProvider;
import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.app.views.character.CharacterSanityChecker;
import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.app.views.character.sheet.CharacterSheetView;
import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.character.classes.ClassArchetype;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.accordion.Accordion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/**
 * A view for editing a {@link PlayerCharacter}. This view is only accessible to the user who owns the character. The
 * owner of the character can edit the character's name, abilities, and other relevant information.
 */
@Route(value = "editor")
@PermitAll
public class CharacterEditorView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private final transient PlayerCharacterService characterService;
    private final CharacterSanityChecker sanityChecker;
    private final transient ClassManagerWrapper classManager;
    private final transient User currentUser;
    private final transient TranslationProvider translationProvider;
    private CharacterSaver characterSaver;
    private Accordion accordion;
    private transient PlayerCharacter pc;
    private AbilitiesEditorView abilitiesEditorView;


    /**
     * Creates a new CharacterEditorView (constructor for Spring injection).
     *
     * @param characterService the service for managing database access to player characters (injected by
     *         Spring)
     * @param authenticatedUser the currently authenticated user (injected by Spring)
     * @param classManager the class manager keeping track of all available classes (injected by Spring)
     * @param i18nProvider the provider for internationalization (injected by Spring)
     */
    public CharacterEditorView(PlayerCharacterService characterService, AuthenticatedUser authenticatedUser,
                               ClassManagerWrapper classManager, I18NProvider i18nProvider) {
        this.characterService = characterService;
        this.currentUser =
                authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not " + "authenticated"));
        this.sanityChecker = new CharacterSanityChecker(characterService, currentUser);
        this.classManager = classManager;
        this.translationProvider = new UserBasedTranslationProvider(currentUser, i18nProvider);
        add(getTranslation("error.character_not_found"));
    }

    private void init() {
        removeAll();
        setAlignItems(Alignment.CENTER);

        add(new H1(getTranslation("character_editor.title")));

        createAccordion();
        add(accordion);

        Button saveButton = new Button(getTranslation("generic.save"), e -> save());
        add(saveButton);
    }

    private void createAccordion() {
        this.accordion = new Accordion();
        accordion.add("Description", new Span(pc.getName()));
        this.abilitiesEditorView = new AbilitiesEditorView(pc);
        accordion.add(getTranslation("character_editor.abilities.title"), abilitiesEditorView);
        accordion.addOpenedChangeListener(e -> abilitiesEditorView.update());
        HorizontalLayout classLayout = new HorizontalLayout();
        for (ClassArchetype<?> archetype : classManager.getRegisteredClasses()) {
            Button classButton = new Button(archetype.getName().getTranslation(translationProvider));
            classButton.addClickListener(e -> {
                pc.getClasses().forEach(pc::removeClass);
                pc.addClass(archetype.getNewInstance());
            });
            classLayout.add(classButton);
        }
        accordion.add("Class", classLayout);
    }

    @Override
    public String getPageTitle() {
        return "Character Editor";
    }

    @Override
    public void setParameter(BeforeEvent event, Long parameter) {
        sanityChecker.ensureSanity(parameter);
        //noinspection OptionalGetWithoutIsPresent
        this.pc = characterService.getCharacter(parameter).get();
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
