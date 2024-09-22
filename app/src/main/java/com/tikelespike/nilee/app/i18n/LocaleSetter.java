package com.tikelespike.nilee.app.i18n;

import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.core.data.entity.User;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.vaadin.flow.server.VaadinServlet;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.servlet.ServletContext;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;

import java.util.Locale;
import java.util.Optional;

/**
 * Localization is achieved in two ways in Nilee. Vaadin components use the getTranslation() method which uses the
 * locale associated with the UI instance the components are displayed in. There's also the
 * {@link com.tikelespike.nilee.core.i18n.TranslationProvider TranslationProvider} and
 * {@link com.tikelespike.nilee.core.i18n.LocalizedString LocalizedString} interfaces which can be used outside of
 * vaadin components.
 * <p>
 * This class makes sure that the locale of the vaadin UI instances is always set to the current users preferred locale
 * as set in the users preferences, so that
 * {@link com.vaadin.flow.component.Component#getTranslation(Object, Object...)} uses the correct locale.
 * <p>
 * See {@link UserBasedTranslationProvider} for the translation provider that can be used independently of Vaadin.
 */
@SpringComponent
public class LocaleSetter implements UIInitListener, VaadinServiceInitListener {

    private final AuthenticationContext authenticationContext;

    /**
     * Creates a new locale setter.
     *
     * @param authenticationContext the authentication context to use to get the current user (injected by
     *         Spring)
     */
    public LocaleSetter(AuthenticationContext authenticationContext) {
        this.authenticationContext = authenticationContext;
    }

    @Override
    public void uiInit(UIInitEvent event) {
        // Don't fully understand this, security risk? Was sort of recommended to me by GitHub Copilot
        ServletContext servletContext = VaadinServlet.getCurrent().getServletContext();
        WebApplicationContext context =
                SecurityWebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
        AuthenticatedUser authUser = context.getBean(AuthenticatedUser.class);
        Optional<User> currentUser = authUser.get();

        currentUser.ifPresent(user -> {
            Locale preferredLocale = user.getPreferredLocale();
            if (preferredLocale != null) {
                event.getUI().setLocale(preferredLocale);
            }
        });
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(this);
    }
}
