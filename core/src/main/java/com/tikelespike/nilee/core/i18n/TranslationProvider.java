package com.tikelespike.nilee.core.i18n;

import java.util.Locale;

/**
 * Is able to resolve keys to locale-specific strings, and provides information about the currently used locale.
 */
public interface TranslationProvider {

    default String translate(String key, Object... params) {
        return translate(key, getLocale(), params);
    }

    String translate(String key, Locale locale, Object... params);

    Locale getLocale();

}
