package com.tikelespike.nilee.views.mainmenu;

import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.data.service.UserRepository;
import com.tikelespike.nilee.data.service.UserService;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import javax.annotation.security.PermitAll;
import java.util.Locale;

@PageTitle("User Preferences")
@Route(value = "preferences", layout = MainLayout.class)
@PermitAll
public class UserPreferencesView extends FormLayout {


    public UserPreferencesView(I18NProvider i18NProvider, UserService userService,
                               AuthenticatedUser authenticatedUser) {
        User user = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not authenticated"));

        ComboBox<Locale> localeComboBox = new ComboBox<>("Language");
        localeComboBox.setItems(i18NProvider.getProvidedLocales());
        localeComboBox.setItemLabelGenerator(l -> l.getDisplayLanguage(getLocale()));
        localeComboBox.addValueChangeListener(e -> user.setPreferredLocale(e.getValue()));
        if (user.getPreferredLocale() != null) {
            localeComboBox.setValue(user.getPreferredLocale());
        }

        Button saveButton = new Button("Save");
        saveButton.addClickListener(e -> {
            userService.update(user);
            VaadinSession.getCurrent().setLocale(user.getPreferredLocale());
            getUI().ifPresent(ui -> ui.getPage().reload());
        });

        add(localeComboBox, saveButton);
    }
}
