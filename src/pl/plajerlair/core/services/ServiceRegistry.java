package pl.plajerlair.core.services;

import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Class for registering new services
 */
public class ServiceRegistry {

    private static List<JavaPlugin> registeredPlugins = new ArrayList<>();
    private static Map<JavaPlugin, Long> serviceCooldown = new HashMap<>();

    public static boolean registerService(JavaPlugin plugin){
        if(registeredPlugins.contains(plugin)){
            return false;
        }
        registeredPlugins.add(plugin);
        plugin.getLogger().log(Level.INFO, "Hooked with ServiceRegistry! Initialized services properly!");
        new MetricsService(plugin);
        return true;
    }

    public static List<JavaPlugin> getRegisteredPlugins() {
        return registeredPlugins;
    }

    public static Map<JavaPlugin, Long> getServiceCooldown() {
        return serviceCooldown;
    }
}
