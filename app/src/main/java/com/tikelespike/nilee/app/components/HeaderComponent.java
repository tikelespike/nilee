package com.tikelespike.nilee.app.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 * A component that can be used to display a sticky header at the top of the page. The header is
 * divided into three sections: left, center and right. Components can be added to these sections individually.
 */
public class HeaderComponent extends HorizontalLayout {

    private final HorizontalLayout leftLayout = new HorizontalLayout();
    private final HorizontalLayout centerLayout = new HorizontalLayout();
    private final HorizontalLayout rightLayout = new HorizontalLayout();

    /**
     * Creates a new header component.
     */
    public HeaderComponent() {
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setAlignItems(Alignment.CENTER);

        // set the style of the header, so it always stays at the top of the page
        getStyle().set("position", "sticky");
        getStyle().set("top", "0");
        getStyle().set("z-index", "99");

        getStyle().set("background-color", "var(--lumo-base-color)");

        leftLayout.setAlignItems(Alignment.CENTER);
        leftLayout.setJustifyContentMode(JustifyContentMode.START);

        centerLayout.setAlignItems(Alignment.CENTER);
        centerLayout.getStyle().set("position", "absolute");
        centerLayout.getStyle().set("left", "50%");
        centerLayout.getStyle().set("transform", "translateX(-50%)");
        centerLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        rightLayout.setAlignItems(Alignment.CENTER);
        rightLayout.setJustifyContentMode(JustifyContentMode.END);

        add(leftLayout, centerLayout, rightLayout);


    }

    /**
     * Adds one or more components to the left-aligned section of the header. The components will be added from
     * left to right and can be removed using {@link #remove(Component)}. The components will be added to a
     * {@link HorizontalLayout} that is part of the header.
     *
     * @param components the components to add
     */
    public void addLeft(Component... components) {
        leftLayout.add(components);
    }

    /**
     * Adds one or more components to the centered section of the header. The centered section is placed such that
     * its center aligns horizontally with the center of the page. As this will result in overlapping components
     * if the page is not wide enough, it is recommended to use this section only if there is enough space.
     * <p>
     * The components will be added from
     * left to right and can be removed using {@link #remove(Component)}. The components will be added to a
     * {@link HorizontalLayout} that is part of the header.
     *
     * @param components the components to add
     */
    public void addCenter(Component... components) {
        centerLayout.add(components);
    }

    /**
     * Adds one or more components to the right-aligned section of the header. The components will be added from
     * left to right and can be removed using {@link #remove(Component)}. The components will be added to a
     * {@link HorizontalLayout} that is part of the header.
     *
     * @param components the components to add
     */
    public void addRight(Component... components) {
        rightLayout.add(components);
    }

    /**
     * Removes a component from the header. The component must have been added to the header using one of the
     * {@code add} methods. If the component is not a child of the header, an {@link IllegalArgumentException}
     * will be thrown.
     *
     * @param component the component to remove
     * @throws IllegalArgumentException if the component has not been added to the header previously
     */
    public void remove(Component component) {
        boolean removed = false;
        if (leftLayout.getElement().equals(component.getElement().getParent())) {
            leftLayout.remove(component);
            removed = true;
        }
        if (centerLayout.getElement().equals(component.getElement().getParent())) {
            centerLayout.remove(component);
            removed = true;
        }
        if (rightLayout.getElement().equals(component.getElement().getParent())) {
            rightLayout.remove(component);
            removed = true;
        }
        if (!removed) {
            throw new IllegalArgumentException("Component is not a child of this header");
        }
    }
}
