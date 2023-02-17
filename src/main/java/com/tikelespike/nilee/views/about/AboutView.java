package com.tikelespike.nilee.views.about;

import com.tikelespike.nilee.data.entity.PlayerCharacter;
import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.tikelespike.nilee.views.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("About")
@Route(value = "about", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends HorizontalLayout {

    private TextField name;
    private Button sayHello;

    private PlayerCharacter pc;
    private Label characterNameLabel;

    public AboutView(UserService userService, AuthenticatedUser authenticatedUser) {
        name = new TextField("Your name");
        sayHello = new Button("Say hello");
        sayHello.addClickListener(e -> {
            Notification.show("Hello " + name.getValue());
            User currentUser = authenticatedUser.get().get();
            currentUser.setName(name.getValue());
            userService.update(currentUser);
        });
        sayHello.addClickShortcut(Key.ENTER);
        characterNameLabel = new Label("No character selected");

        setMargin(true);
        setVerticalComponentAlignment(Alignment.END, name, sayHello);

        add(name, sayHello, characterNameLabel);
    }

    public static String getPath() {
        return "about";
    }

    public void setPlayerCharacter(PlayerCharacter pc) {
        this.pc = pc;
        characterNameLabel.setText(pc.getName());
    }

    public PlayerCharacter getPlayerCharacter() {
        return pc;
    }
}
