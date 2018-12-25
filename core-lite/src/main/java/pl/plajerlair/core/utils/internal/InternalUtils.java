package pl.plajerlair.core.utils.internal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

/**
 * Only for core usages
 */
public class InternalUtils {

    public static void errorOccurred() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "  _____                                                                                  _   _ ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " | ____|  _ __   _ __    ___    _ __      ___     ___    ___   _   _   _ __    ___    __| | | |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " |  _|   | '__| | '__|  / _ \\  | '__|    / _ \\   / __|  / __| | | | | | '__|  / _ \\  / _` | | |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " | |___  | |    | |    | (_) | | |      | (_) | | (__  | (__  | |_| | | |    |  __/ | (_| | |_|");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " |_____| |_|    |_|     \\___/  |_|       \\___/   \\___|  \\___|  \\__,_| |_|     \\___|  \\__,_| (_)");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "                                                                                               ");
    }

    public static String toReadable(String version) {
        String[] split = Pattern.compile(".", Pattern.LITERAL).split(version.replace("v", ""));
        StringBuilder versionBuilder = new StringBuilder();
        for(String s : split) {
            versionBuilder.append(String.format("%4s", s));
        }
        version = versionBuilder.toString();
        return version;
    }

}
