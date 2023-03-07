package com.tikelespike.nilee.views.mainmenu;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route(value = "about", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@AnonymousAllowed
public class AboutView extends VerticalLayout {

    public AboutView() {
        setAlignItems(Alignment.CENTER);

        add(new H2(getTranslation("about.title")));
        add("by Timo Weberruß");
        add(new Anchor("https://github.com/tikelespike/nilee", "https://github.com/tikelespike/nilee"));
        add("This is a work in progress.");
    }
}
