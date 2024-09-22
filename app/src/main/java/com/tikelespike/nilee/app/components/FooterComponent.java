package com.tikelespike.nilee.app.components;

/**
 * A component that can be used to display a sticky header at the bottom of the page. The footer is divided into three
 * sections: left, center and right. Components can be added to these sections individually.
 */
public class FooterComponent extends BarComponent {

    /**
     * Creates a new footer component.
     */
    public FooterComponent() {
        super();
        getStyle().set("background-color", "var(--lumo-base-color)");
        getStyle().set("position", "fixed");
        getStyle().set("bottom", "0");
        getStyle().set("left", "0");
    }
}
