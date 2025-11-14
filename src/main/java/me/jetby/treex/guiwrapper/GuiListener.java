package me.jetby.treex.guiwrapper;

import me.jetby.treex.guiwrapper.itemwrapper.ItemWrapper;
import me.jetby.treex.guiwrapper.itemwrapper.LegacyWrapper;
import me.jetby.treex.guiwrapper.itemwrapper.ModernWrapper;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
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

        ItemWrapper itemWrapper = gui.getItems().get(slot);
        if (itemWrapper instanceof LegacyWrapper wrapper) {
            if (wrapper.onClick() != null) {
                wrapper.onClick().accept(e);
            }
        } else if (itemWrapper instanceof ModernWrapper wrapper) {
            if (wrapper.onClick() != null) {
                wrapper.onClick().accept(e);
            }
        }
    }

    @EventHandler
    public void onDrag(InventoryDragEvent e) {
        XGui gui = getHolder(e.getInventory());
        if (gui == null) return;

        if (gui.onDrag()!=null) {
            gui.onDrag().accept(e);
        }

    }

    @EventHandler
    public void onOpen(InventoryOpenEvent e) {
        XGui gui = getHolder(e.getInventory());
        if (gui == null) return;

        if (gui.onOpen()!=null) {
            gui.onOpen().accept(e);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e) {
        XGui gui = getHolder(e.getInventory());
        if (gui == null) return;

        if (gui.onClose()!=null) {
            gui.onClose().accept(e);
        }
    }

    @Nullable
    private static XGui getHolder(Inventory inventory) {
        if (inventory == null) return null;

        InventoryHolder holder = inventory.getHolder();
        if (holder == null) return null;

        return holder instanceof XGui ? ((XGui) holder) : null;
    }

}
