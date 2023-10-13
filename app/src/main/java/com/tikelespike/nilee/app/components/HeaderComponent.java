package com.tikelespike.nilee.app.components;

/**
 * A component that can be used to display a sticky header at the top of the page. The header is
 * divided into three sections: left, center and right. Components can be added to these sections individually.
 */
public class HeaderComponent extends BarComponent {

    public HeaderComponent() {
        super();
        getStyle().set("background-color", "var(--lumo-base-color)");
        getStyle().set("top", "0");
    }
}
