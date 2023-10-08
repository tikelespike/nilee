package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.components.RemoteUIManager;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.game.GameSession;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.UUID;

public class SessionDialog extends Dialog {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";

    private final User user;

    private final EventBus eventBus = new EventBus();

    private HorizontalLayout footerContent;

    private final RemoteUIManager remoteUIManager = new RemoteUIManager();

    public SessionDialog(User user) {
        this.user = user;
        update();
        user.getSession().addUserJoinedListener(e -> remoteUIManager.execute(this::update));
        user.getSession().addUserLeftListener(e -> {
            if (!user.equals(e.getUser())) remoteUIManager.execute(this::update);
        });
    }

    private void update() {
        removeAll();
        getFooter().removeAll();

        setHeaderTitle("Connect to other players");
        HorizontalLayout currentSessionLayout = createCurrentSessionLayout();
        add(currentSessionLayout);
        footerContent = createFooter();
        getFooter().add(footerContent);
    }

    private HorizontalLayout createFooter() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        layout.setWidthFull();

        AvatarGroup avatarGroup = new AvatarGroup();
        for (User participant : user.getSession().getParticipants()) {
            AvatarGroup.AvatarGroupItem avatar = new AvatarGroup.AvatarGroupItem(participant.getName());
            StreamResource resource = new StreamResource("profile-pic",
                    () -> new ByteArrayInputStream(participant.getProfilePicture()));
            avatar.setImageResource(resource);
            avatarGroup.add(avatar);
        }
        layout.add(avatarGroup);


        Button leaveButton = new Button();
        leaveButton.addClickListener(e -> {
            user.leaveCurrentSession();
            update();
        });


        if (user.getSession().getParticipants().size() > 1) {
            addClassName("success-footer");
            leaveButton.setText("Leave session");
            layout.add(leaveButton);
        } else {
            Button joinButton = new Button("Join session");
            joinButton.addClickListener(e -> openJoinDialog());
            leaveButton.setText("New session");
            layout.add(leaveButton, joinButton);
        }

        return layout;
    }

    private void openJoinDialog() {
        Dialog joinDialog = new Dialog();
        joinDialog.setHeaderTitle("Join session");
        HorizontalLayout joinLayout = new HorizontalLayout();
        joinLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        TextField joinSessionTF = new TextField("Session ID");
        joinSessionTF.setWidth("25em");
        joinSessionTF.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        joinSessionTF.setPattern(UUID_REGEX);
        joinSessionTF.setAutoselect(true);
        joinLayout.add(joinSessionTF);

        Button joinButton = new Button("Join");
        joinButton.addClickListener(e -> {
            if (!joinSessionTF.getValue().matches(UUID_REGEX)) {
                joinSessionTF.setInvalid(true);
                showErrorMessage("Invalid session ID Format");
                return;
            }
            UUID sessionId = UUID.fromString(joinSessionTF.getValue());
            if (!user.canJoin(sessionId)) {
                joinSessionTF.setInvalid(true);
                showErrorMessage("Session does not exist");
                return;
            }
            user.joinSession(sessionId);
            joinDialog.close();
            update();
            eventBus.fireEvent(new SessionJoinedEvent(user.getSession()));
        });
        joinLayout.add(joinButton);

        joinDialog.add(joinLayout);
        joinDialog.open();
    }

    public Registration addSessionJoinedListener(EventListener<SessionJoinedEvent> listener) {
        return eventBus.registerListener(SessionJoinedEvent.class, listener);
    }

    private HorizontalLayout createCurrentSessionLayout() {
        HorizontalLayout currentSessionLayout = new HorizontalLayout();
        currentSessionLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        TextField currentSessionTF = new TextField("Current session");
        currentSessionTF.setReadOnly(true);
        currentSessionTF.setValue(user.getSession().getId().toString());
        currentSessionTF.setWidth("25em");
        currentSessionTF.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        currentSessionTF.setAutoselect(true);
        currentSessionLayout.add(currentSessionTF);
        currentSessionLayout.expand(currentSessionTF);

        Button copyButton = new Button("Copy");
        copyButton.addClickListener(e -> {
            currentSessionTF.focus();
            currentSessionTF.getElement().executeJs("navigator.clipboard.writeText($0.value)", currentSessionTF);
        });
        currentSessionLayout.add(copyButton);
        return currentSessionLayout;
    }

    private static void showErrorMessage(String text) {
        Notification errorMessage = new Notification(text, 3000);
        errorMessage.addThemeVariants(NotificationVariant.LUMO_ERROR);
        errorMessage.setPosition(Notification.Position.TOP_CENTER);
        errorMessage.open();
    }

    public static class SessionJoinedEvent extends Event {
        private final GameSession newSession;

        public SessionJoinedEvent(GameSession newSession) {
            this.newSession = newSession;
        }

        public GameSession getNewSession() {
            return newSession;
        }
    }
}
