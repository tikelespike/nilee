package com.tikelespike.nilee.core.i18n;

import java.util.Locale;

/**
 * Is able to resolve keys to locale-specific strings, and provides information about the currently used locale.
 */
public interface TranslationProvider {

    /**
     * Resolves a key string to a concrete, localized string, filling in the given parameters at the appropriate
     * places.
     * <p>
     * This is a convenience method that uses {@link #translate(String, Locale, Object...)} with the locale from
     * {@link #getLocale()}.
     *
     * @param key unique identifier of the string to get a localized version for
     * @param params parameters (usually strings) that are filled into the resulting string at predefined
     *         places
     *
     * @return a concrete string that can be displayed to users, in the current locale returned by {@link #getLocale()}
     */
    default String translate(String key, Object... params) {
        return translate(key, getLocale(), params);
    }

    /**
     * Resolves a key string to a concrete, localized string, filling in the given parameters at the appropriate
     * places.
     *
     * @param key unique identifier of the string to get a localized version for
     * @param locale target locale of the returned concrete strings
     * @param params parameters (usually strings) that are filled into the resulting string at predefined
     *         places
     *
     * @return a concrete string that can be displayed to users, in the current locale returned by {@link #getLocale()}
     */
    String translate(String key, Locale locale, Object... params);

    /**
     * Returns the locale this translation provider currently provides translations for.
     *
     * @return the current locale
     */
    Locale getLocale();

}
