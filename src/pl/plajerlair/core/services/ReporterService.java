package pl.plajerlair.core.services;

import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Instant;

/**
 * Reporter service
 */
public class ReporterService {

    private String plugin;
    private String pluginVersion;
    private String serverVersion;
    private String error;

    //don't create it outside core
    ReporterService(String plugin, String pluginVersion, String serverVersion, String error) {
        this.plugin = plugin;
        this.pluginVersion = pluginVersion;
        this.serverVersion = serverVersion;
        this.error = error;
    }

    public void reportException() {
        try {
            String query = "pass=servicereporter&type=" + URLEncoder.encode(plugin, "UTF-8") + "&pluginversion=" + URLEncoder.encode(pluginVersion, "UTF-8") +
                    "&serverversion=" + URLEncoder.encode(serverVersion, "UTF-8") + "&error=" + URLEncoder.encode(error, "UTF-8") + "&creationdate=" + Instant.now().getEpochSecond();
            StringBuffer buffer = new StringBuffer("https://plajer.xyz/errorservice/report.php?");
            buffer.append(query);
            URL url = new URL(buffer.toString());
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setConnectTimeout(60000);
            conn.setRequestMethod("GET");
            Bukkit.getConsoleSender().sendMessage("[Reporter service] Error reported! Code: " + conn.getResponseCode() + " (" + conn.getResponseMessage() + ")");
        } catch(IOException ignored) {/*cannot connect or there is a problem*/}
    }

}
