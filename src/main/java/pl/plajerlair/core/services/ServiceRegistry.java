package pl.plajerlair.core.services;

import org.bukkit.plugin.java.JavaPlugin;
import pl.plajerlair.core.services.locale.LocaleService;
import pl.plajerlair.core.services.metrics.MetricsService;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Class for registering new services
 */
public class ServiceRegistry {

    private static JavaPlugin registeredService;
    private static boolean serviceEnabled;
    private static long serviceCooldown = 0;
    private static LocaleService localeService;

    public static boolean registerService(JavaPlugin plugin) {
        if(registeredService.equals(plugin)) {
            return false;
        }
        try {
            plugin.getLogger().log(Level.INFO, "Connecting to services, please wait! Server may freeze a bit!");
            if(!InetAddress.getByName("https://api.plajer.xyz/locale/fetch.php").isReachable(3 * 1000)) {
                plugin.getLogger().log(Level.WARNING, "Plajer's Lair services are online or inaccessible from your location!");
                serviceEnabled = false;
                return false;
            }
        } catch(IOException ignored) {
            plugin.getLogger().log(Level.WARNING, "Plajer's Lair services are online or inaccessible from your location!");
            serviceEnabled = false;
            return false;
        }
        registeredService = plugin;
        serviceEnabled = true;
        plugin.getLogger().log(Level.INFO, "Hooked with ServiceRegistry! Initialized services properly!");
        new MetricsService(plugin);
        localeService = new LocaleService(plugin);
        return true;
    }

    public static JavaPlugin getRegisteredService() {
        return registeredService;
    }

    public static long getServiceCooldown() {
        return serviceCooldown;
    }

    public static void setServiceCooldown(long serviceCooldown) {
        ServiceRegistry.serviceCooldown = serviceCooldown;
    }

    public static LocaleService getLocaleService(JavaPlugin plugin) {
        if(!serviceEnabled || registeredService == null || !registeredService.equals(plugin)) {
            return null;
        }
        return localeService;
    }

    public static boolean isServiceEnabled() {
        return serviceEnabled;
    }
}
