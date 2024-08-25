package com.tikelespike.nilee.app.i18n;

import com.tikelespike.nilee.core.data.entity.User;
import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.i18n.I18NProvider;

import java.util.Locale;

/**
 * Provides a locale to use based on the currently logged-in user and their preferences set. The actual lookup is
 * then done using an injected {@link I18NProvider}.
 */
public class UserBasedTranslationProvider implements TranslationProvider {
    private final User user;
    private final I18NProvider i18nProvider;

    public UserBasedTranslationProvider(User user, I18NProvider i18nProvider) {
        this.user = user;
        this.i18nProvider = i18nProvider;
    }

    @Override
    public String translate(String key, Locale locale, Object... params) {
        return i18nProvider.getTranslation(key, locale, params);
    }

    @Override
    public Locale getLocale() {
        return user != null && user.getPreferredLocale() != null ? user.getPreferredLocale() : Locale.ENGLISH;
    }
}
