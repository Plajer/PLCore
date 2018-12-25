package pl.plajerlair.core.services.exception;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import pl.plajerlair.core.services.ServiceRegistry;

/**
 * Create reported exception with data sent to plajer.xyz reporter service
 */
public class ReportedException {

    private ReporterService reporterService;

    public ReportedException(JavaPlugin plugin, Exception e) {
        e.printStackTrace();
        if(!ServiceRegistry.isServiceEnabled()) {
            return;
        }
        if(System.currentTimeMillis() - ServiceRegistry.getServiceCooldown() < 900000) {
            return;
        }
        if(plugin.getDescription().getVersion().contains("b")) {
            return;
        }
        ServiceRegistry.setServiceCooldown(System.currentTimeMillis());
        new BukkitRunnable() {
            @Override
            public void run() {
                StringBuffer stacktrace = new StringBuffer(e.getClass().getSimpleName());
                if(e.getMessage() != null) {
                    stacktrace.append(" (").append(e.getMessage()).append(")");
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
