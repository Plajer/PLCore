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
import java.io.FileInputStream;
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
        if(ServiceRegistry.getRegisteredService() == null || !ServiceRegistry.getRegisteredService().equals(plugin)) {
            throw new IllegalArgumentException("LocaleService cannot be used without registering service via ServiceRegistry first!");
        }
        if(!ServiceRegistry.isServiceEnabled()) {
            return;
        }
        this.plugin = plugin;
        try {
            String data = IOUtils.toString(requestLocaleFetch(null), StandardCharsets.UTF_8);
            FileUtils.write(new File(plugin.getDataFolder().getPath() + "/locales/locale_data.yml"), data, StandardCharsets.UTF_8);
            this.localeData = ConfigUtils.getConfig(plugin, "/locales/locale_data");
            plugin.getLogger().log(Level.INFO, "Fetched latest localization file from repository.");
        } catch(IOException ignored) {
            //ignore exceptions
            plugin.getLogger().log(Level.WARNING, "Couldn't access locale fetcher service or there is other problem! You should notify author!");
        }
    }

    private InputStream requestLocaleFetch(Locale locale) {
        try {
            //todo /v2/
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
                os.write(("pass=localeservice&type=" + plugin.getName() + "&locale=" + locale.getPrefix()).getBytes(StandardCharsets.UTF_8));
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
     * @param locale locale to download
     * @return SUCCESS for downloaded locale, FAIL for service fault, LATEST when locale is latest as one in repository
     */
    public DownloadStatus demandLocaleDownload(Locale locale) {
        //service fault
        if(localeData == null) {
            return DownloadStatus.FAIL;
        }
        File localeFile = new File(plugin.getDataFolder() + "/locales/" + locale.getPrefix() + ".properties");
        if(!localeFile.exists() || !isExact(locale, localeFile)) {
            return writeFile(locale);
        }
        return DownloadStatus.LATEST;
    }

    private DownloadStatus writeFile(Locale locale) {
        try {
            String data = IOUtils.toString(requestLocaleFetch(locale), StandardCharsets.UTF_8);
            FileUtils.write(new File(plugin.getDataFolder().getPath() + "/locales/" + locale.getPrefix() + ".properties"), data, StandardCharsets.UTF_8);
            return DownloadStatus.SUCCESS;
        } catch(IOException ignored) {
            plugin.getLogger().log(Level.WARNING, "Demanded locale " + locale.getPrefix() + " cannot be downloaded! You should notify author!");
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
        return !checkHigher(plugin.getDescription().getVersion(), localeData.getString("locales.valid-version", plugin.getDescription().getVersion()));
    }

    private boolean isExact(Locale locale, File file) {
        try {
            String onlineData = IOUtils.toString(requestLocaleFetch(locale), StandardCharsets.UTF_8);
            String localData = IOUtils.toString(new FileInputStream(file), StandardCharsets.UTF_8);

            return onlineData.equals(localData);
        } catch(IOException ignored) {
            return false;
        }
    }

    private boolean checkHigher(String currentVersion, String newVersion) {
        String current = InternalUtils.toReadable(currentVersion);
        String newVer = InternalUtils.toReadable(newVersion);
        return current.compareTo(newVer) < 0;
    }

    /**
     * Download status enum for locale download demands
     */
    public enum DownloadStatus {
        SUCCESS, FAIL, LATEST
    }

}
