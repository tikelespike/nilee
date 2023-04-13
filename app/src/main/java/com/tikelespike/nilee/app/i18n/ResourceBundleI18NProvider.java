package com.tikelespike.nilee.app.i18n;

import com.tikelespike.nilee.core.i18n.TranslationProvider;
import com.vaadin.flow.i18n.I18NProvider;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.UIInitEvent;
import com.vaadin.flow.server.UIInitListener;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

@Component
public class ResourceBundleI18NProvider implements I18NProvider, TranslationProvider, UIInitListener, VaadinServiceInitListener {

    private static final String[] BUNDLE_PREFIXES = {"text/ui_strings", "text/core_strings"};

    private Locale currentLocale = Locale.ENGLISH;

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

    @Override
    public String translate(String key, Locale locale, Object... params) {
        return getTranslation(key, locale, params);
    }

    @Override
    public Locale getLocale() {
        return currentLocale;
    }

    @Override
    public void uiInit(UIInitEvent event) {
        currentLocale = event.getUI().getLocale();
    }

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(this);
    }
}
