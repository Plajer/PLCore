package pl.plajerlair.core.utils;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;

/**
 * Check updates of plugin
 */
public class UpdateChecker {

    private static String latestVersion;

    private static boolean checkHigher(String currentVersion, String newVersion) {
        String current = InternalUtils.toReadable(currentVersion);
        String newVer = InternalUtils.toReadable(newVersion);
        return current.compareTo(newVer) < 0;
    }

    /**
     * Check current version of plugin at SpigotMC website
     * if version is beta (contains "b") plugin will check config of requesting plugin to check if
     * "Update-Notifier.Notify-Beta-Versions" is true
     *
     * @param plugin         requesting plugin
     * @param currentVersion current plugin version from plugin.yml
     * @param resourceID     spigotmc resource ID to check
     */
    public static void checkUpdate(JavaPlugin plugin, String currentVersion, int resourceID) {
        String version = getVersion(resourceID);
        if(version.contains("b")) {
            if(!plugin.getConfig().getBoolean("Update-Notifier.Notify-Beta-Versions", true)) {
                return;
            }
        }
        if(checkHigher(currentVersion, version)) {
            latestVersion = version;
        }
    }

    /**
     * Get latest version of plugin from spigotmc
     *
     * @return latest version from spigotmc, return null when version is same (latest)
     */
    public static String getLatestVersion() {
        return latestVersion;
    }

    private static String getVersion(int ver) {
        String version = null;
        try {
            HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=" + ver).openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(("resource=" + ver).getBytes("UTF-8"));
            version = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        } catch(IOException ignored) {
        }
        return version;
    }

}
