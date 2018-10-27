package pl.plajerlair.core.services.locale;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.plajerlair.core.services.ServiceRegistry;
import pl.plajerlair.core.utils.ConfigUtils;
import pl.plajerlair.core.utils.internal.InternalUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

/**
 * Localization service used for fetching latest locales for minigames
 */
public class LocaleService {

    private JavaPlugin plugin;
    private FileConfiguration localeData;

    public LocaleService(JavaPlugin plugin) {
        if(!ServiceRegistry.getRegisteredPlugins().contains(plugin)) {
            throw new IllegalArgumentException("LocaleReaderService cannot be used without registering service via ServiceRegistry first!");
        }
        this.plugin = plugin;
        try {
            String data = IOUtils.toString(requestLocaleFetch(null), StandardCharsets.UTF_8);
            FileUtils.write(new File(plugin.getDataFolder().getPath() + "/locales/data.yml"), data, StandardCharsets.UTF_8);
            if(!new File(plugin.getDataFolder().getPath() + "/locales/origin_data.yml").exists()) {
                FileUtils.write(new File(plugin.getDataFolder().getPath() + "/locales/origin_data.yml"), data, StandardCharsets.UTF_8);
            }
            this.localeData = ConfigUtils.getConfig(plugin, "/locales/data");
            //-1 cause not including "valid-version" param
            plugin.getLogger().log(Level.INFO, "Fetched latest localizations, " + (localeData.getConfigurationSection("locales").getKeys(false).size() - 1) + " locales were fetched.");
        } catch(IOException ignored) {
            //ignore exceptions
            plugin.getLogger().log(Level.WARNING, "Couldn't access locale fetcher service or there is other problem! You should notify author!");
        }
    }

    private InputStream requestLocaleFetch(String locale) {
        try {
            URL url = new URL("https://api.plajer.xyz/locale/fetch.php");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "PLLocale/1.0");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            if(locale == null) {
                os.write(("pass=localeservice&type=" + plugin.getName()).getBytes(StandardCharsets.UTF_8));
            } else {
                os.write(("pass=localeservice&type=" + plugin.getName() + "&locale=" + locale).getBytes(StandardCharsets.UTF_8));
            }
            os.flush();
            os.close();
            return conn.getInputStream();
        } catch(Exception e) {
            e.printStackTrace();
            return new InputStream() {
                @Override
                public int read() {
                    return -1;
                }
            };
        }
    }


    /**
     * Sends a demand request to download latest locale from Plajer-Lair/locale_storage repository
     * Whole repository can be seen here https://github.com/Plajer-Lair/locale_storage
     *
     * @param locale locale prefix to download
     * @return SUCCESS for downloaded locale, FAIL for service fault, LATEST when locale is latest as one in repository
     */
    public DownloadStatus demandLocaleDownload(String locale) {
        //service fault
        if(localeData == null) {
            return DownloadStatus.FAIL;
        }
        if(!new File(plugin.getDataFolder() + "/locales/" + locale + ".properties").exists()) {
            return writeFile(locale);
        }
        if(localeData.getInt("locales." + locale + ".version") > ConfigUtils.getConfig(plugin, "/locales/origin_data").getInt("locales." + locale + ".version", -1)) {
            return writeFile(locale);
        } else {
            return DownloadStatus.LATEST;
        }
    }

    private DownloadStatus writeFile(String locale) {
        try {
            String data = IOUtils.toString(requestLocaleFetch(locale), StandardCharsets.UTF_8);
            FileUtils.write(new File(plugin.getDataFolder().getPath() + "/locales/" + locale + ".properties"), data, StandardCharsets.UTF_8);

            FileConfiguration config = ConfigUtils.getConfig(plugin, "/locales/origin_data");
            config.set("locales." + locale, localeData.getInt("locales." + locale, 0));
            ConfigUtils.saveConfig(plugin, config, "/locales/origin_data");
            return DownloadStatus.SUCCESS;
        } catch(IOException ignored) {
            plugin.getLogger().log(Level.WARNING, "Demanded locale " + locale + " cannot be downloaded! You should notify author!");
            return DownloadStatus.FAIL;
        }
    }

    /**
     * Checks if plugin version allows to update locale
     *
     * @return true if locale can be updated for this version else cannot
     */
    public boolean isValidVersion() {
        //service fault
        if(localeData == null) {
            return false;
        }
        return !checkHigher(plugin.getDescription().getVersion(), localeData.getString("locales.valid-version", "0"));
    }

    private boolean checkHigher(String currentVersion, String newVersion) {
        String current = InternalUtils.toReadable(currentVersion);
        String newVer = InternalUtils.toReadable(newVersion);
        return current.compareTo(newVer) < 0;
    }

    /**
     * Returns requested locale version number
     *
     * @param locale locale prefix
     * @return locale version
     */
    public int getLocaleVersion(String locale) {
        //service fault
        if(localeData == null) {
            return 0;
        }
        return localeData.getInt("locales." + locale + ".version", 69);
    }

    /**
     * Download status enum for locale download demands
     */
    public enum DownloadStatus {
        SUCCESS, FAIL, LATEST
    }

}
