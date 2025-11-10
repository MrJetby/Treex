package me.jetby.treex.gui;

import me.jetby.treex.gui.itemwrapper.LegacyWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Nullable;

public class GuiListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        XGui gui = getHolder(e.getInventory());
        if (gui == null) return;

        int slot = e.getRawSlot();
        if (slot < 0 || slot >= e.getInventory().getSize()) return;

        LegacyWrapper wrapper = gui.getItems().get(slot);
        if (wrapper != null && wrapper.onClick() != null) {
            wrapper.onClick().accept(e);
        }
    }

    @Nullable
    public static XGui getHolder(Inventory inventory) {
        if (inventory == null) return null;

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) return null;

        return holder instanceof XGui ? ((XGui) holder) : null;
    }

}
