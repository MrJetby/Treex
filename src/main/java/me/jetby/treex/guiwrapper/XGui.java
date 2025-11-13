package me.jetby.treex.guiwrapper;

import lombok.Getter;
import me.jetby.treex.guiwrapper.itemwrapper.ItemWrapper;
import me.jetby.treex.guiwrapper.itemwrapper.LegacyWrapper;
import me.jetby.treex.guiwrapper.itemwrapper.ModernWrapper;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public abstract class XGui implements InventoryHolder {

    protected Inventory inventory;
    @Getter
    private final Map<Integer, ItemWrapper> items = new HashMap<>();

    public XGui(@Nullable GuiForm form) {
        if (form.getMenu()==null) return;
        this.inventory = form.getMenu().inventoryType() != null ?
                Bukkit.createInventory(this, form.getMenu().inventoryType()) :
                Bukkit.createInventory(this, form.getMenu().size(), form.getMenu().title());
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public ItemWrapper getWrapper(int slot) {
        return items.get(slot);
    }

    public void unregisterItem(int slot) {
        var item = items.get(slot);
        if (item!=null) {
            inventory.setItem(slot, null);
            items.remove(slot);
        }
    }
    public void registerItem(int slot, ItemWrapper itemWrapper) {
        if (itemWrapper instanceof LegacyWrapper wrapper) {
            registerLegacyItemWrapper(slot, wrapper);
        } else if (itemWrapper instanceof ModernWrapper wrapper) {
            registerModernItemWrapper(slot, wrapper);
        } else {
            throw new RuntimeException("Does not match any of the possible formats");
        }
    }

    private void registerModernItemWrapper(int slot, ModernWrapper wrapper) {
        ItemStack itemStack = wrapper.itemStack();
        if (itemStack == null) {
            itemStack = new ItemStack(wrapper.material());
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                meta.displayName(wrapper.displayName());
                meta.lore(wrapper.lore());
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
    private void registerLegacyItemWrapper(int slot, LegacyWrapper wrapper) {
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

    private int currentPage = 0;
    public void openPage(int page) {
        currentPage = page;
    }
    public void nextPage() {

    }
    public void prevPage() {

    }

    public Player player;

    public void open(@NotNull Player player) {
        this.player = player;
        player.openInventory(inventory);
    }
}
