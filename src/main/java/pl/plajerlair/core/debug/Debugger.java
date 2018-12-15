package pl.plajerlair.core.debug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * @author Plajer
 * <p>
 * Created at 15.12.2018
 *
 * Class for printing debug messages
 */
public class Debugger {

    private static boolean enabled = false;
    private static String prefix = "";

    public static void setPrefix(String prefix) {
        Debugger.prefix = prefix;
    }

    public static void setEnabled(boolean enabled) {
        Debugger.enabled = enabled;
    }

    /**
     * Prints debug message with selected log level
     * @param level level of debugged message
     * @param thing debugged message
     */
    public static void debug(LogLevel level, String thing) {
        if (enabled) {
            switch (level) {
                case INFO:
                    Bukkit.getConsoleSender().sendMessage(prefix + " " + thing);
                    break;
                case WARN:
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + prefix + " " + thing);
                    break;
                case ERROR:
                    Bukkit.getConsoleSender().sendMessage(ChatColor.RED + prefix + " " + thing);
                    break;
                case WTF:
                    Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + prefix + " [SEVERE]" + thing);
                    break;
                case TASK:
                    Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + prefix + " Running task '" + thing + "'");
                    break;
            }
        }
    }

}
