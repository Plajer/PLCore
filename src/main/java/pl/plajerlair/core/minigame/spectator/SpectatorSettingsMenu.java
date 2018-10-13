package pl.plajerlair.core.minigame.spectator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import pl.plajerlair.core.utils.ItemBuilder;
import pl.plajerlair.core.utils.XMaterial;

/**
 * Spectator settings menu for minigames
 * Initialize it as normal class and keep instance in your plugin
 *
 * @since 1.3.0
 */
public class SpectatorSettingsMenu implements Listener {

  private String inventoryName;
  private String speedOptionName;
  private String nightvisionOptionName;
  private Inventory inv;

  public SpectatorSettingsMenu(JavaPlugin plugin, String inventoryName, String speedOptionName, String nightvisionOptionName) {
    this.inventoryName = inventoryName;
    this.speedOptionName = speedOptionName;
    this.nightvisionOptionName = nightvisionOptionName;
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
    initInventory();
  }

  public void openSpectatorSettingsMenu(Player player) {
    player.openInventory(this.inv);
  }

  @EventHandler
  public void onSpectatorMenuClick(InventoryClickEvent e) {
    if (e.getInventory() == null || !e.getInventory().getName().equals(color(inventoryName))) {
      return;
    }
    if (e.getCurrentItem() == null || !e.getCurrentItem().hasItemMeta()) {
      return;
    }
    Player p = (Player) e.getWhoClicked();
    p.closeInventory();
    if (e.getCurrentItem().getType() == Material.LEATHER_BOOTS) {
      p.removePotionEffect(PotionEffectType.SPEED);
      p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0, false, false));
    } else if (e.getCurrentItem().getType() == Material.CHAINMAIL_BOOTS) {
      p.removePotionEffect(PotionEffectType.SPEED);
      p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false));
    } else if (e.getCurrentItem().getType() == Material.IRON_BOOTS) {
      p.removePotionEffect(PotionEffectType.SPEED);
      p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 2, false, false));
    } else if (e.getCurrentItem().getType() == XMaterial.GOLDEN_BOOTS.parseMaterial()) {
      p.removePotionEffect(PotionEffectType.SPEED);
      p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 3, false, false));
    } else if (e.getCurrentItem().getType() == Material.DIAMOND_BOOTS) {
      p.removePotionEffect(PotionEffectType.SPEED);
      p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 4, false, false));
    } else if (e.getCurrentItem().getType() == Material.ENDER_PEARL) {
      p.removePotionEffect(PotionEffectType.NIGHT_VISION);
      p.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1, false, false));
      inv.removeItem(new ItemStack(Material.ENDER_PEARL));
      inv.setItem(20, new ItemBuilder(new ItemStack(Material.EYE_OF_ENDER, 1))
          .name(color(nightvisionOptionName + " OFF")).build());
    } else if (e.getCurrentItem().getType() == Material.EYE_OF_ENDER) {
      p.removePotionEffect(PotionEffectType.NIGHT_VISION);
      inv.setItem(20, new ItemBuilder(new ItemStack(Material.ENDER_PEARL, 1))
          .name(color(nightvisionOptionName + " ON")).build());
    }
  }

  private void initInventory() {
    Inventory inv = Bukkit.createInventory(null, 9 * 4, inventoryName);
    inv.setItem(11, new ItemBuilder(new ItemStack(Material.LEATHER_BOOTS, 1))
        .name(color(speedOptionName + " I")).build());
    inv.setItem(12, new ItemBuilder(new ItemStack(Material.CHAINMAIL_BOOTS, 1))
        .name(color(speedOptionName + " II")).build());
    inv.setItem(13, new ItemBuilder(new ItemStack(Material.IRON_BOOTS, 1))
        .name(color(speedOptionName + " III")).build());
    inv.setItem(14, new ItemBuilder(XMaterial.GOLDEN_BOOTS.parseItem())
        .name(color(speedOptionName + " IV")).build());
    inv.setItem(15, new ItemBuilder(new ItemStack(Material.DIAMOND_BOOTS, 1))
        .name(color(speedOptionName + " V")).build());
    inv.setItem(20, new ItemBuilder(new ItemStack(Material.ENDER_PEARL, 1))
        .name(color(nightvisionOptionName + " ON")).build());
    this.inv = inv;
  }

  private String color(String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }

}
