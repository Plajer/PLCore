package pl.plajerlair.core.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class for building ItemStacks
 *
 * @since 2.0.0
 */
public class ItemBuilder {

    private final ItemStack is;

    public ItemBuilder(final ItemStack is) {
        this.is = is;
    }

    public ItemBuilder name(final String name) {
        final ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(name);
        is.setItemMeta(meta);
        return this;
    }

    public ItemBuilder lore(final String... name) {
        final ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) {
            lore = new ArrayList<>();
        }
        lore.addAll(Arrays.asList(name));
        meta.setLore(lore);
        is.setItemMeta(meta);
        return this;
    }

    public ItemStack build() {
        return is;
    }

}
