package com.tikelespike.nilee.app.components;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class HeaderComponent extends HorizontalLayout {

    private final HorizontalLayout leftLayout = new HorizontalLayout();
    private final HorizontalLayout centerLayout = new HorizontalLayout();
    private final HorizontalLayout rightLayout = new HorizontalLayout();

    public HeaderComponent() {
        setWidthFull();
        setJustifyContentMode(JustifyContentMode.BETWEEN);
        setAlignItems(Alignment.CENTER);

        // set the style of the header so it always stays at the top of the page
        getStyle().set("position", "sticky");
        getStyle().set("top", "0");
        getStyle().set("z-index", "99");

        getStyle().set("background-color", "var(--lumo-base-color)");

        leftLayout.setAlignItems(Alignment.CENTER);
        leftLayout.setJustifyContentMode(JustifyContentMode.START);
        centerLayout.setAlignItems(Alignment.CENTER);
        centerLayout.getStyle().set("position", "absolute");
        centerLayout.getStyle().set("left", "50%");
        leftLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        rightLayout.setAlignItems(Alignment.CENTER);
        leftLayout.setJustifyContentMode(JustifyContentMode.END);

        add(leftLayout, centerLayout, rightLayout);
    }

    public void addLeft(Component... components) {
        for (Component component : components) {
            addLeft(component);
        }
    }

    private void addLeft(Component component) {
        leftLayout.add(component);
    }

    public void addCenter(Component... components) {
        for (Component component : components) {
            addCenter(component);
        }
    }

    private void addCenter(Component component) {
        centerLayout.add(component);
    }

    public void addRight(Component... components) {
        for (Component component : components) {
            addRight(component);
        }
    }

    private void addRight(Component component) {
        rightLayout.add(component);
    }

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
