package com.tikelespike.nilee.app.i18n;

import com.vaadin.flow.i18n.I18NProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

@Component
public class ResourceBundleI18NProvider implements I18NProvider {

    public static final String BUNDLE_PREFIX = "text/ui_strings";

    @Override
    public List<Locale> getProvidedLocales() {
        return Collections.unmodifiableList(Arrays.asList(Locale.ENGLISH, Locale.GERMAN));
    }

    @Override
    public String getTranslation(String key, Locale locale, Object... params) {
        if (key == null) {
            LoggerFactory.getLogger(ResourceBundleI18NProvider.class.getName()).warn("Got lang request for key with null value!");
            return "";
        }

        final ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_PREFIX, locale);

        String value;
        try {
            value = bundle.getString(key);
        } catch (final MissingResourceException e) {
            LoggerFactory.getLogger(ResourceBundleI18NProvider.class.getName()).warn("Missing resource", e);
            return "!" + locale.getLanguage() + ": " + key;
        }
        if (params.length > 0) {
            value = MessageFormat.format(value, params);
        }
        return value;
    }
}
