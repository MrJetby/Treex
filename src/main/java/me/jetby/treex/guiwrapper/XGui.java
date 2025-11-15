package me.jetby.treex.guiwrapper;

import lombok.Getter;
import me.jetby.treex.guiwrapper.item.ItemWrapper;
import me.jetby.treex.guiwrapper.item.wrappers.LegacyWrapper;
import me.jetby.treex.guiwrapper.item.wrappers.ModernWrapper;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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
import java.util.function.Consumer;

public abstract class XGui implements InventoryHolder {

    @Getter
    private Inventory inventory;
    @Getter
    private final Map<Integer, ItemWrapper> items = new HashMap<>();

    private Consumer<InventoryDragEvent> onDrag;
    private Consumer<InventoryOpenEvent> onOpen;
    private Consumer<InventoryCloseEvent> onClose;
    public Consumer<InventoryDragEvent> onDrag() {return onDrag;}
    public Consumer<InventoryOpenEvent> onOpen() {return onOpen;}
    public Consumer<InventoryCloseEvent> onClose() {return onClose;}
    public void onDrag(Consumer<InventoryDragEvent> event) {}
    public void onOpen(Consumer<InventoryOpenEvent> event) {}
    public void onClose(Consumer<InventoryCloseEvent> event) {}

    public Player player;

    public void open(@NotNull Player player) {
        this.player = player;
        player.openInventory(inventory);
    }

    public XGui(@Nullable GuiForm form) {
        if (form.getMenu()==null) return;
        this.inventory = form.getMenu().inventoryType() != null ?
                Bukkit.createInventory(this, form.getMenu().inventoryType()) :
                Bukkit.createInventory(this, form.getMenu().size(), form.getMenu().title());
    }

    public XGui(@NotNull String title) {
        this.inventory = Bukkit.createInventory(this, InventoryType.CHEST, title);
    }
    public XGui(@NotNull String title, int size) {
        this.inventory = Bukkit.createInventory(this, size, title);
    }
    public XGui(@NotNull String title, InventoryType inventoryType) {
        this.inventory = Bukkit.createInventory(this, inventoryType, title);
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
    public void registerItem(ItemWrapper itemWrapper) {
        if (itemWrapper instanceof LegacyWrapper wrapper) {
            registerLegacyItemWrapper(wrapper);
        } else if (itemWrapper instanceof ModernWrapper wrapper) {
            registerModernItemWrapper(wrapper);
        } else {
            throw new RuntimeException("Does not match any of the possible formats");
        }
    }

    private void registerModernItemWrapper(ModernWrapper wrapper) {
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
        inventory.setItem(wrapper.slot(), itemStack);
        items.put(wrapper.slot(), wrapper);
    }
    private void registerLegacyItemWrapper(LegacyWrapper wrapper) {
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
        } else {
            ItemMeta meta = itemStack.getItemMeta();
            if (meta != null) {
                if (wrapper.displayName()!=null) meta.setDisplayName(wrapper.displayName());
                if (wrapper.lore()!=null) meta.setLore(wrapper.lore());
            }
        }
        inventory.setItem(wrapper.slot(), itemStack);
        items.put(wrapper.slot(), wrapper);
    }
}
