package pl.plajerlair.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * Only for core usages
 */
public class MessageUtils {

    static void errorOccured() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "  _____                                                                                  _   _ ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " | ____|  _ __   _ __    ___    _ __      ___     ___    ___   _   _   _ __    ___    __| | | |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " |  _|   | '__| | '__|  / _ \\  | '__|    / _ \\   / __|  / __| | | | | | '__|  / _ \\  / _` | | |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " | |___  | |    | |    | (_) | | |      | (_) | | (__  | (__  | |_| | | |    |  __/ | (_| | |_|");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " |_____| |_|    |_|     \\___/  |_|       \\___/   \\___|  \\___|  \\__,_| |_|     \\___|  \\__,_| (_)");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "                                                                                               ");
    }


}
