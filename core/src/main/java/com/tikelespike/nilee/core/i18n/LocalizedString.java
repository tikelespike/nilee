package com.tikelespike.nilee.core.i18n;

/**
 * A locale-independent String that depends on a {@link TranslationProvider} in order to resolve to a locale-specific,
 * regular String.
 * <p>
 * This way, a String can be abstracted from its language-specific parts.
 */
public interface LocalizedString {

    /**
     * Translates this abstract/locale-independent string into a specific locale given a translation provider that
     * resolves the locale-specific parts of this string.
     * <p>
     * This function must be pure, that is, it must not have side effects and must always return the same result for the
     * same input.
     *
     * @param translationProvider a translation provider translating locale-specific strings
     *
     * @return the string resolved to a locale-specific string
     */
    String getTranslation(TranslationProvider translationProvider);

}
