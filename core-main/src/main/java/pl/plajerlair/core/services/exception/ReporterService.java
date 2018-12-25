package pl.plajerlair.core.services.exception;

import org.bukkit.Bukkit;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Reporter service for reporting exceptions directly to website reporter panel
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
            //todo /v2/
            URL url = new URL("https://api.plajer.xyz/error/report.php");
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("User-Agent", "PLService/1.0");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(("pass=servicereporter&type=" + plugin + "&pluginversion=" + pluginVersion + "&serverversion=" + serverVersion + "&error=" + error).getBytes(StandardCharsets.UTF_8));
            os.flush();
            os.close();

            Bukkit.getConsoleSender().sendMessage("[Reporter service] Error reported! Code: " + conn.getResponseCode() + " (" + conn.getResponseMessage() + ")");
        } catch(IOException ignored) {/*cannot connect or there is a problem*/}
    }

}
