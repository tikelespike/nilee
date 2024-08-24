package com.tikelespike.nilee.app.views.character.sheet;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.events.Event;
import com.tikelespike.nilee.core.events.EventBus;
import com.tikelespike.nilee.core.events.EventListener;
import com.tikelespike.nilee.core.events.Registration;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.avatar.AvatarGroup;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.server.StreamResource;

import java.io.ByteArrayInputStream;
import java.util.UUID;

public class SessionDialog extends Dialog {

    private static final String UUID_REGEX = "^[0-9a-fA-F]{8}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{4}\\b-[0-9a-fA-F]{12}$";

    private final TranslationProvider translationProvider;

    private final User user;

    private final EventBus eventBus = new EventBus();

    public SessionDialog(TranslationProvider translationProvider, User user) {
        this.translationProvider = translationProvider;
        this.user = user;
        update();
    }

    public void update() {
        removeAll();
        getFooter().removeAll();
        getHeader().removeAll();

        setHeaderTitle(translationProvider.translate("character_sheet.sessions.dialog.title"));

        Button closeButton = new Button(new Icon("lumo", "cross"), e -> close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        getHeader().add(closeButton);

        HorizontalLayout currentSessionLayout = createCurrentSessionLayout();
        add(currentSessionLayout);
        HorizontalLayout footerContent = createFooter();
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
            Button leaveButton = new Button(
                    translationProvider.translate("character_sheet.sessions.dialog.leave_button"));
            leaveButton.addClickListener(e -> eventBus.fireEvent(new LeaveClickedEvent()));
            leaveButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
            layout.add(leaveButton);
        } else {
            Button joinButton = new Button(
                    translationProvider.translate("character_sheet.sessions.dialog.join_button"));
            joinButton.addClickListener(e -> openJoinDialog());
            joinButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);

            Button newSessionButton = new Button(
                    translationProvider.translate("character_sheet.sessions.dialog.new_button"));
            newSessionButton.addClickListener(e -> eventBus.fireEvent(new NewSessionClickedEvent()));
            layout.add(newSessionButton, joinButton);
        }

        return layout;
    }

    private void openJoinDialog() {
        Dialog joinDialog = new Dialog();
        joinDialog.setHeaderTitle(translationProvider.translate("character_sheet.sessions.join_dialog.title"));
        HorizontalLayout joinLayout = new HorizontalLayout();
        joinLayout.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.END);

        Button closeButton = new Button(new Icon("lumo", "cross"), e -> joinDialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        joinDialog.getHeader().add(closeButton);

        TextField joinSessionTF = new TextField(
                translationProvider.translate("character_sheet.sessions.join_dialog.id_field.label"));
        joinSessionTF.setWidth("25em");
        joinSessionTF.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER, TextFieldVariant.LUMO_HELPER_ABOVE_FIELD);
        joinSessionTF.setPattern(UUID_REGEX);
        joinSessionTF.setAutoselect(true);
        joinSessionTF.focus();
        joinLayout.add(joinSessionTF);

        Button joinButton = new Button(
                translationProvider.translate("character_sheet.sessions.join_dialog.join_button"));
        joinButton.addClickListener(e -> {
            if (!joinSessionTF.getValue().matches(UUID_REGEX)) {
                joinSessionTF.setInvalid(true);
                joinSessionTF.setHelperText(
                        translationProvider.translate("character_sheet.sessions.join_dialog.error.session_format"));
                return;
            }
            UUID sessionId = UUID.fromString(joinSessionTF.getValue());
            if (!user.canJoin(sessionId)) {
                joinSessionTF.setInvalid(true);
                joinSessionTF.setHelperText(
                        translationProvider.translate("character_sheet.sessions.join_dialog.error.unknown_session"));
                return;
            }
            eventBus.fireEvent(new JoinClickedEvent(sessionId));
            joinDialog.close();
        });
        joinButton.addClickShortcut(Key.ENTER);
        joinButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        joinLayout.add(joinButton);

        joinDialog.add(joinLayout);
        joinDialog.open();
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

        TextField currentSessionTF = new TextField(
                translationProvider.translate("character_sheet.sessions.dialog.current_field.label"));
        currentSessionTF.setReadOnly(true);
        currentSessionTF.setValue(user.getSession().getId().toString());
        currentSessionTF.setWidth("25em");
        currentSessionTF.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        currentSessionTF.setAutoselect(true);
        currentSessionLayout.add(currentSessionTF);
        currentSessionLayout.expand(currentSessionTF);

        Button copyButton = new Button(translationProvider.translate("generic.copy"));
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
