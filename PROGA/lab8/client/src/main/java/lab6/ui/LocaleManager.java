package lab6.ui;

import java.util.*;

public class LocaleManager {
    private final Map<Locale, ResourceBundle> bundles = new HashMap<>();
    private static final String BUNDLE_BASE = "i18n.locale";

    public LocaleManager(){
        loadBundle(new Locale("en"));
        loadBundle(new Locale("ru"));
        loadBundle(new Locale("el"));
        loadBundle(new Locale("mk"));
        loadBundle(new Locale("es"));
    }


    public void loadBundle(Locale locale) {
        if (!bundles.containsKey(locale)) {
            try {
                ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_BASE + "_" + locale.getLanguage(), locale);
                bundles.put(locale, bundle);
            } catch (Exception e) {
            }
        }
    }

    public ResourceBundle getBundle(Locale locale) {
        return bundles.get(locale);
    }

    public Set<Locale> getLocales(){
        return bundles.keySet();
    }
}
