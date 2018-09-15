package pl.plajerlair.core.services.locale;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry class for holding locales
 *
 * @since 1.2.0
 */
public class LocaleRegistry {

    private static List<Locale> registeredLocales = new ArrayList<>();

    public static void registerLocale(Locale locale) {
        if(registeredLocales.contains(locale)) {
            throw new IllegalArgumentException("Cannot register same locale twice!");
        }
        registeredLocales.add(locale);
    }

    public static List<Locale> getRegisteredLocales() {
        return registeredLocales;
    }
}
