package com.tikelespike.nilee.app.i18n;

import com.tikelespike.nilee.app.security.AuthenticatedUser;
import com.tikelespike.nilee.core.data.entity.User;
import com.vaadin.flow.server.*;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Locale;
import java.util.Optional;

@SpringComponent
public class LocaleSetter implements UIInitListener, VaadinServiceInitListener {

    AuthenticationContext authenticationContext;

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
