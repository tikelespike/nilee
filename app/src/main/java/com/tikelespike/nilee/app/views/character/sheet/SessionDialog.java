package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.app.components.RemoteUIManager;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.vaadin.flow.component.Key;
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
    }

    public void update() {
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


        if (user.getSession().getParticipants().size() > 1) {
            Button leaveButton = new Button("Leave session");
            leaveButton.addClickListener(e -> eventBus.fireEvent(new LeaveClickedEvent()));
            addClassName("success-footer");
            layout.add(leaveButton);
        } else {
            Button joinButton = new Button("Join session");
            joinButton.addClickListener(e -> openJoinDialog());

            Button newSessionButton = new Button("New session");
            newSessionButton.addClickListener(e -> eventBus.fireEvent(new NewSessionClickedEvent()));
            layout.add(newSessionButton, joinButton);
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
        joinSessionTF.focus();
        joinLayout.add(joinSessionTF);

        Button joinButton = new Button("Join");
        joinButton.addClickListener(e -> {
            if (!joinSessionTF.getValue().matches(UUID_REGEX)) {
                joinSessionTF.setInvalid(true);
                joinSessionTF.setHelperText("Invalid session ID Format");
                return;
            }
            UUID sessionId = UUID.fromString(joinSessionTF.getValue());
            if (!user.canJoin(sessionId)) {
                joinSessionTF.setInvalid(true);
                joinSessionTF.setHelperText("Session does not exist");
                return;
            }
            eventBus.fireEvent(new JoinClickedEvent(sessionId));
            joinDialog.close();
        });
        joinButton.addClickShortcut(Key.ENTER);
        joinLayout.add(joinButton);

        joinDialog.add(joinLayout);
        joinDialog.open();
    }

    private void showErrorMessage(String errorMessage) {
        Notification notification = new Notification(errorMessage, 3000);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
        notification.open();
    }

    public Registration addJoinClickedListener(EventListener<JoinClickedEvent> listener) {
        return eventBus.registerListener(JoinClickedEvent.class, listener);
    }

    public Registration addLeaveClickedListener(EventListener<LeaveClickedEvent> listener) {
        return eventBus.registerListener(LeaveClickedEvent.class, listener);
    }

    public Registration addNewSessionClickedListener(EventListener<NewSessionClickedEvent> listener) {
        return eventBus.registerListener(NewSessionClickedEvent.class, listener);
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

    public static class JoinClickedEvent extends Event {

        private final UUID newSessionID;

        public JoinClickedEvent(UUID newSessionID) {
            this.newSessionID = newSessionID;
        }

        public UUID getNewSessionID() {
            return newSessionID;
        }
    }

    public static class LeaveClickedEvent extends Event {
    }

    public static class NewSessionClickedEvent extends Event {
    }
}
