package me.jetby.treex.gui;

import me.jetby.treex.gui.itemwrapper.LegacyWrapper;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ItemController {

    private LegacyWrapper legacyWrapper;
    private InventoryClickEvent onClick;


    public void itemWrapper(LegacyWrapper legacyWrapper) {
        this.legacyWrapper = legacyWrapper;
    }

    public void onClick(InventoryClickEvent onClick) {
        this.onClick = onClick;
    }
}
