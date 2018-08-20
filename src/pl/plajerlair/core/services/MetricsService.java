package pl.plajerlair.core.services;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URL;

/**
 * @author Plajer
 * <p>
 * Created at 20.08.2018
 */
public class MetricsService {

    private JavaPlugin plugin;

    MetricsService(JavaPlugin plugin) {
        this.plugin = plugin;
        metricsSchedulerTask();
    }

    public void metricsSchedulerTask() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://plajer.xyz/metricsservice/receiver.php");
                    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("User-Agent", "Mozilla/5.0");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);

                    OutputStream os = conn.getOutputStream();
                    os.write(("pass=metricsservice&type=" + plugin.getName() + "&pluginversion=" + plugin.getDescription().getVersion() +
                            "&serverversion=" + plugin.getServer().getBukkitVersion() + "&ip=" + InetAddress.getLocalHost().getHostAddress() + ":" + plugin.getServer().getPort() +
                            "&playersonline=" + Bukkit.getOnlinePlayers().size()).getBytes("UTF-8"));
                    os.flush();
                    os.close();
                } catch(IOException ignored) {/*cannot connect or there is a problem*/}
            }
        }.runTaskTimerAsynchronously(plugin, 20 * 60 * 30, 20 * 60 * 30);
    }

}
