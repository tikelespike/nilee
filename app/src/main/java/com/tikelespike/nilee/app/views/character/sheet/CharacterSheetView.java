package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.components.BarComponent;
import com.tikelespike.nilee.app.components.FooterComponent;
import com.tikelespike.nilee.app.components.HeaderComponent;
import com.tikelespike.nilee.app.components.RemoteUIManager;
import com.tikelespike.nilee.app.i18n.UserBasedTranslationProvider;
import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.app.views.character.CharacterSanityChecker;
import com.tikelespike.nilee.app.views.character.CharacterSaver;
import com.tikelespike.nilee.app.views.character.editor.CharacterEditorView;
import com.tikelespike.nilee.app.views.character.sheet.dice.RollAnimator;
import com.tikelespike.nilee.app.views.mainmenu.CharacterListView;
import com.tikelespike.nilee.core.character.PlayerCharacter;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.PlayerCharacterService;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.game.GameSession;
import com.tikelespike.nilee.core.game.RollBus;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.TabSheet;
import com.vaadin.flow.component.tabs.TabSheetVariant;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.stream.Collectors;

/**
 * A view that displays a character sheet for a {@link PlayerCharacter} for use during play. The user can view the
 * character's abilities, hit points, and other relevant information, and interact with the character by initiating
 * rolls and other actions. The user can also join or leave a game session to share the character with other players.
 */
@Route(value = "sheet")
@PermitAll
public class CharacterSheetView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private static final int SESSION_NOTIFICATION_DURATION = 3000;
    private static final int PLACEHOLDER_TEXT_REPETITIONS = 20;
    private final transient PlayerCharacterService characterService;

    private final CharacterSanityChecker sanityChecker;
    private final transient TranslationProvider translationProvider;

    private final transient User currentUser;
    private final RemoteUIManager uiManager = new RemoteUIManager();
    private final RollAnimator rollAnimator;

    private transient PlayerCharacter pc;
    private CharacterSaver characterSaver;

    private Icon sessionIcon;

    private transient Registration userJoinedRegistration;
    private transient Registration userLeftRegistration;
    private SessionDialog sessionDialog;

    /**
     * Creates a new CharacterSheetView (constructor for Spring injection).
     *
     * @param authenticatedUser the currently authenticated user (injected by Spring)
     * @param characterService the service for managing database access to player characters (injected by
     *         Spring)
     * @param i18nProvider the provider for translations (injected by Spring)
     */
    public CharacterSheetView(AuthenticatedUser authenticatedUser, PlayerCharacterService characterService,
                              I18NProvider i18nProvider) {
        this.characterService = characterService;
        this.currentUser =
                authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not " + "authenticated"));
        this.sanityChecker = new CharacterSanityChecker(characterService, currentUser);
        this.translationProvider = new UserBasedTranslationProvider(currentUser, i18nProvider);

        // initialization happens in setParameter based on the given character
        add(getTranslation("error.character_not_found"));

        // prevent scrolling down so the header disappears
        getStyle().set("position", "sticky");
        getStyle().set("top", "0");

        addAttachListener(e -> register());
        addDetachListener(e -> unregister());
        rollAnimator = new RollAnimator(translationProvider);
    }


    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter Long parameter) {
        sanityChecker.ensureSanity(parameter);
        //noinspection OptionalGetWithoutIsPresent
        update(characterService.getCharacter(parameter).get());
    }

    private void update(PlayerCharacter playerCharacter) {
        this.pc = playerCharacter;
        this.characterSaver = new CharacterSaver(playerCharacter, characterService, sanityChecker);

        removeAll();
        setPadding(true);
        Component header = createHeader();
        add(header);

        TabSheet tabSheet = createTabSheet();
        add(tabSheet);

        Div expander = new Div();
        expand(expander);
        add(expander);

        BarComponent footer = createFooter();
        add(footer);

        setHeightFull();

        if (sessionDialog != null) {
            sessionDialog.update();
        }
    }

    private BarComponent createFooter() {
        BarComponent footer = new FooterComponent();
        footer.getStyle().set("background-color", "transparent");
        footer.getStyle().set("padding-bottom", "1em");
        footer.getStyle().set("padding-left", "1em");
        footer.getStyle().set("padding-right", "1em");

        sessionIcon = new Icon(VaadinIcon.CONNECT);
        sessionIcon.setSize("2.5em");
        sessionIcon.getStyle().set("margin", "1em");
        sessionIcon.setColor("var(--lumo-tint-20pct)");
        Button sessionButton = new Button(sessionIcon);
        sessionButton.setHeightFull();
        sessionButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        sessionButton.addClickListener(e -> {
            sessionDialog = new SessionDialog(translationProvider, currentUser);
            sessionDialog.addJoinClickedListener(this::joinSession);
            sessionDialog.addLeaveClickedListener(this::leaveSession);
            sessionDialog.addNewSessionClickedListener(this::newSession);
            sessionDialog.open();
        });
        updateSessionIcon();
        footer.addLeft(sessionButton);

        return footer;
    }

    private void joinSession(SessionDialog.JoinClickedEvent event) {
        unregister();
        currentUser.joinSession(event.getNewSessionID());
        update(pc);
        register();
        sessionDialog.close();
        Notification successNotification =
                new Notification(translationProvider.translate("character_sheet.sessions.join_message"),
                        SESSION_NOTIFICATION_DURATION);
        successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        successNotification.setPosition(Notification.Position.TOP_CENTER);
        uiManager.open(successNotification);
    }

    private void leaveSession(SessionDialog.LeaveClickedEvent event) {
        unregister();
        currentUser.leaveCurrentSession();
        update(pc);
        register();
        sessionDialog.close();
        Notification leftNotification =
                new Notification(translationProvider.translate("character_sheet.sessions.leave_message"),
                        SESSION_NOTIFICATION_DURATION);
        leftNotification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        leftNotification.setPosition(Notification.Position.TOP_CENTER);
        uiManager.open(leftNotification);
    }

    private void newSession(SessionDialog.NewSessionClickedEvent event) {
        unregister();
        currentUser.leaveCurrentSession();
        update(pc);
        register();
        Notification notification =
                new Notification(translationProvider.translate("character_sheet.sessions.new_message"),
                        SESSION_NOTIFICATION_DURATION);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        notification.setPosition(Notification.Position.TOP_CENTER);
        uiManager.open(notification);
    }

    private void onOtherUserJoined(GameSession.UserJoinedEvent event) {
        Notification notification = new Notification(
                translationProvider.translate("character_sheet.sessions.other_joined_message",
                        event.getNewUser().getName()), SESSION_NOTIFICATION_DURATION);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        notification.setPosition(Notification.Position.TOP_CENTER);
        // for some reason the notification instantly disappears if not delayed since vaadin 24
        // giving it a slight delay seems to fix this issue
        uiManager.open(notification, 1);
        uiManager.execute(() -> update(pc));
    }

    private void onOtherUserLeft(GameSession.UserLeftEvent event) {
        Notification notification = new Notification(
                translationProvider.translate("character_sheet.sessions.other_left_message", event.getUser().getName()),
                SESSION_NOTIFICATION_DURATION);
        notification.addThemeVariants(NotificationVariant.LUMO_CONTRAST);
        notification.setPosition(Notification.Position.TOP_CENTER);
        uiManager.open(notification, 1);
        uiManager.execute(() -> update(pc));
    }

    private void unregister() {
        if (userJoinedRegistration != null) {
            userJoinedRegistration.unregisterAll();
        }
        if (userLeftRegistration != null) {
            userLeftRegistration.unregisterAll();
        }
    }

    private void register() {
        userJoinedRegistration = currentUser.getSession().addUserJoinedListener(this::onOtherUserJoined);
        userLeftRegistration = currentUser.getSession().addUserLeftListener(this::onOtherUserLeft);
    }

    private void updateSessionIcon() {
        if (currentUser.getSession().getParticipants().size() > 1) {
            uiManager.execute(() -> sessionIcon.setColor("var(--lumo-success-color)"));
        } else {
            uiManager.execute(() -> sessionIcon.setColor("var(--lumo-tint-20pct)"));
        }
    }

    private TabSheet createTabSheet() {
        Text placeholderText2 = new Text(
                ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " + "tempor incididunt"
                        + " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation "
                        + "ullamco "
                        + "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                        + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat "
                        + "cupidatat non "
                        + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.").repeat(
                        PLACEHOLDER_TEXT_REPETITIONS));
        Text placeholderText3 = new Text(
                ("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod " + "tempor incididunt"
                        + " ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation "
                        + "ullamco "
                        + "laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in "
                        + "voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat "
                        + "cupidatat non "
                        + "proident, sunt in culpa qui officia deserunt mollit anim id est laborum.").repeat(
                        PLACEHOLDER_TEXT_REPETITIONS));
        RollBus rollBus = currentUser.getSession().getRollBus();
        rollAnimator.setRollBus(rollBus);
        // disconnect roll animator from roll bus when leaving this view, otherwise rollAnimator will not get garbage
        // collected because it still subscribes to roll events -> memory leak
        addDetachListener((ComponentEventListener<DetachEvent>) event -> rollAnimator.setRollBus(null));

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

        H3 nameTitle = new H3(pc.getName() + pc.getClasses().stream()
                .map(c -> c.getArchetype().getName().getTranslation(translationProvider))
                .collect(Collectors.joining(", ", " (", ")")));
        nameTitle.getElement().getStyle().set("margin-top", "0.5em");

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
