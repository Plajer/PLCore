package pl.plajerlair.core.services;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Create reported exception with data sent to plajer.xyz reporter service
 */
public class ReportedException {

    private ReporterService reporterService;

    public ReportedException(JavaPlugin plugin, Exception e) {
        e.printStackTrace();
        if(!ServiceRegistry.getRegisteredPlugins().contains(plugin)) {
            return;
        }
        if(System.currentTimeMillis() - ServiceRegistry.getServiceCooldown().getOrDefault(plugin, 0L) < 900000) {
            return;
        }
        if(plugin.getDescription().getVersion().contains("b")) {
            return;
        }
        ServiceRegistry.getServiceCooldown().put(plugin, System.currentTimeMillis());
        System.out.print(e.getClass().getSimpleName());
        new BukkitRunnable() {
            @Override
            public void run() {
                StringBuffer stacktrace = new StringBuffer(e.getClass().getSimpleName());
                if(e.getCause() != null) {
                    stacktrace.append(" (").append(e.getCause()).append(")");
                }
                stacktrace.append("\n");
                for(StackTraceElement str : e.getStackTrace()) {
                    stacktrace.append(str.toString()).append("\n");
                }
                reporterService = new ReporterService(plugin.getName(), plugin.getDescription().getVersion(), plugin.getServer().getBukkitVersion() + " " + plugin.getServer().getVersion(), stacktrace.toString());
                reporterService.reportException();
            }
        }.runTaskAsynchronously(plugin);
    }

}
