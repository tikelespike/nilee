package com.tikelespike.nilee.app.views.mainmenu;

import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.data.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;

import java.util.Locale;

@Route(value = "preferences", layout = MainLayout.class)
@PermitAll
public class UserPreferencesView extends VerticalLayout implements HasDynamicTitle {


    public UserPreferencesView(I18NProvider i18NProvider, UserService userService,
                               AuthenticatedUser authenticatedUser) {
        User user = authenticatedUser.get().orElseThrow(() -> new IllegalStateException("User not authenticated"));

        FormLayout form = createForm(i18NProvider, userService, user);
        add(form);
        expand(form);

        Button saveButton = createSaveButton(userService, user);
        setHorizontalComponentAlignment(Alignment.STRETCH, saveButton);

        add(saveButton);

        setSizeFull();
    }

    private FormLayout createForm(I18NProvider i18NProvider, UserService userService, User user) {
        FormLayout form = new FormLayout();
        ComboBox<Locale> localeComboBox = createLocaleComboBox(i18NProvider, user);
        form.addFormItem(localeComboBox, getTranslation("preferences.language.label"));
        return form;
    }

    private Button createSaveButton(UserService userService, User user) {
        Button saveButton = new Button(getTranslation("generic.save"));
        saveButton.addClickListener(e -> {
            userService.update(user);
            VaadinSession.getCurrent().setLocale(user.getPreferredLocale());
            getUI().ifPresent(ui -> ui.getPage().reload());
        });
        return saveButton;
    }

    private ComboBox<Locale> createLocaleComboBox(I18NProvider i18NProvider, User user) {
        ComboBox<Locale> localeComboBox = new ComboBox<>();
        localeComboBox.setItems(i18NProvider.getProvidedLocales());
        localeComboBox.setItemLabelGenerator(l -> l.getDisplayLanguage(getLocale()));
        localeComboBox.addValueChangeListener(e -> user.setPreferredLocale(e.getValue()));
        if (user.getPreferredLocale() != null) {
            localeComboBox.setValue(user.getPreferredLocale());
        }
        localeComboBox.addThemeVariants();
        return localeComboBox;
    }

    @Override
    public String getPageTitle() {
        return getTranslation("preferences.title");
    }
}
