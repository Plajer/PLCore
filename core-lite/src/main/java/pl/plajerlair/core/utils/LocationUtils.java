package pl.plajerlair.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Utilities for locations
 */
public class LocationUtils {

    /**
     * Saves location in the file
     *
     * @param plugin   plugin to get data folder from
     * @param file     file object where to save
     * @param fileName file name
     * @param path     path where location will be saved
     * @param loc      location to save
     */
    public static void saveLoc(JavaPlugin plugin, FileConfiguration file, String fileName, String path, Location loc) {
        String location = loc.getWorld().getName() + "," + loc.getX() + "," + loc.getY() + "," + loc.getZ() + "," + loc.getYaw() + "," + loc.getPitch();
        file.set(path, location);
        ConfigUtils.saveConfig(plugin, file, fileName);
    }

    /**
     * Retrieves location from given string
     *
     * @param path location as a string, you can get it by #saveLoc method
     * @return full location (world, x, y, z, yaw, pitch) also without yaw and pitch if not provided in path param
     * @see #saveLoc(JavaPlugin, FileConfiguration, String, String, Location)
     */
    public static Location getLocation(String path) {
        if(path == null || path.split(",").length == 0) {
            throw new IllegalArgumentException("String from which location is retrieved cannot be null nor empty!");
        }
        String[] loc = path.split(",");
        World w = Bukkit.getServer().getWorld(loc[0]);
        double x = Double.parseDouble(loc[1]);
        double y = Double.parseDouble(loc[2]);
        double z = Double.parseDouble(loc[3]);
        if(loc.length > 4) {
            float yaw = Float.parseFloat(loc[4]);
            float pitch = Float.parseFloat(loc[5]);
            return new Location(w, x, y, z, yaw, pitch);
        }
        return new Location(w, x, y, z);
    }

    /**
     * Save location to string ex world,10,20,30
     *
     * @param location location to string
     * @return location saved to string
     */
    public static String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getX() + "," + location.getY() + "," + location.getZ();
    }

}
