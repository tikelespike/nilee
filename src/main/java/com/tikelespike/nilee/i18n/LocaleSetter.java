package com.tikelespike.nilee.i18n;

import com.tikelespike.nilee.data.entity.User;
import com.tikelespike.nilee.security.AuthenticatedUser;
import com.vaadin.flow.server.ServiceException;
import com.vaadin.flow.server.SessionInitEvent;
import com.vaadin.flow.server.SessionInitListener;
import com.vaadin.flow.spring.annotation.SpringComponent;

import java.io.Serial;
import java.util.Locale;
import java.util.Optional;

@SpringComponent
public class LocaleSetter implements SessionInitListener {

    private final Optional<User> currentUser;

    /** serial VUID */
    @Serial
    private static final long serialVersionUID = 7782078275956323697L;

    public LocaleSetter(AuthenticatedUser authenticatedUser) {
        this.currentUser = authenticatedUser.get();
    }

    @Override
    public void sessionInit(SessionInitEvent event) throws ServiceException {
        currentUser.ifPresent(user -> {
            Locale preferredLocale = user.getPreferredLocale();
            if (preferredLocale != null) {
                event.getSession().setLocale(preferredLocale);
            }
        });
    }
}
