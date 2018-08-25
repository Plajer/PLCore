package pl.plajerlair.core.services;

import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

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
            URL url = new URL("https://plajer.xyz/errorservice/report.php");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(("pass=servicereporter&type=" + plugin + "&pluginversion=" + pluginVersion + "&serverversion=" + serverVersion + "&error=" + error).getBytes("UTF-8"));
            os.flush();
            os.close();

            Bukkit.getConsoleSender().sendMessage("[Reporter service] Error reported! Code: " + conn.getResponseCode() + " (" + conn.getResponseMessage() + ")");
        } catch(IOException ignored) {/*cannot connect or there is a problem*/}
    }

}
