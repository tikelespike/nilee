package com.tikelespike.nilee.core.i18n;

import java.util.Locale;

public interface TranslationProvider {

    default String translate(String key, Object... params) {
        return translate(key, getLocale(), params);
    }

    String translate(String key, Locale locale, Object... params);

    Locale getLocale();

}
