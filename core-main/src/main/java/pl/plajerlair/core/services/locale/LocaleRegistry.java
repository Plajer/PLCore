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

    /**
     * Register new locale into registry
     * @param locale locale to register
     * @throws IllegalArgumentException if same locale is registered twice
     */
    public static void registerLocale(Locale locale) {
        if(registeredLocales.contains(locale)) {
            throw new IllegalArgumentException("Cannot register same locale twice!");
        }
        registeredLocales.add(locale);
    }

    /**
     * Get all registered locales
     * @return all registered locales
     */
    public static List<Locale> getRegisteredLocales() {
        return registeredLocales;
    }

    /**
     * Get locale by its name
     * @param name name to search
     * @return locale by name or locale "Undefined" when not found (null is not returned)
     * @since 1.2.2
     */
    public static Locale getByName(String name) {
        for(Locale locale : registeredLocales) {
            if(locale.getName().equals(name)) {
                return locale;
            }
        }
        return new Locale("Undefined", "Undefined", "", "System", new ArrayList<>());
    }
}
