package me.jetby.treex.gui;

import lombok.Getter;
import me.jetby.treex.gui.itemwrapper.LegacyWrapper;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class XGui implements InventoryHolder {

    protected Inventory inventory;
    @Getter
    private final Map<Integer, LegacyWrapper> items = new HashMap<>();

    public XGui(@Nullable InventoryType type) {
        this.inventory = type != null ? Bukkit.createInventory(this, type) : Bukkit.createInventory(this, 9, "title");
    }


    protected void setBase() {
        System.out.println(" ");
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void redraw(int slot) {
        var item = items.get(slot);
        if (item==null) return;
        registerItem(slot, item);
    }

    public void registerItem(int slot, LegacyWrapper wrapper) {
        ItemStack itemStack = wrapper.itemStack();
        if (itemStack == null) {
            itemStack = new ItemStack(wrapper.material());
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(wrapper.displayName());
                meta.setLore(wrapper.lore());
                meta.setCustomModelData(wrapper.customModelData());
                if (wrapper.enchanted()) {
                    meta.addEnchant(Enchantment.OXYGEN, 1, false);
                }
                if (wrapper.flags() != null && !wrapper.flags().isEmpty()) {
                    for (ItemFlag flag : wrapper.flags()) {
                        meta.addItemFlags(flag);
                    }
                }
                itemStack.setItemMeta(meta);
            }
            wrapper.itemStack(itemStack);
        }
        inventory.setItem(slot, itemStack);
        items.put(slot, wrapper);
    }

    private Player player;

    public Player player() {
        return player;
    }

    public void open(Player player) {
        this.player = player;
        player.openInventory(inventory);
    }
}
