package com.tikelespike.nilee.app.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

/**
 * Resolves keys to localized string values given a target locale using {@link ResourceBundle ResourceBundles}.
 */
@Component
public class ResourceBundleI18NProvider implements I18NProvider {

    private static final String[] BUNDLE_PREFIXES = {"text/ui_strings", "text/core_strings"};


    @Override
    public List<Locale> getProvidedLocales() {
        return Collections.unmodifiableList(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(ResourceBundleI18NProvider.class.getName()).warn(
                    "Got lang request for key with null value!");
            return "";
        }

        for (String prefix : BUNDLE_PREFIXES) {
            final ResourceBundle bundle = ResourceBundle.getBundle(prefix, locale);

            if (bundle.containsKey(key)) {
                String value = bundle.getString(key);
                if (params.length > 0) {
                    value = MessageFormat.format(value, params);
                }
                return value;
            }
        }

        LoggerFactory.getLogger(ResourceBundleI18NProvider.class.getName()).warn("Missing resource: " + key);
        return "!" + locale.getLanguage() + ": " + key;
    }
}
