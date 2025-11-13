package me.jetby.treex.gui;

import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public record Menu(
        String id,
        String title,
        int size,
        InventoryType inventoryType,
        String permission,
        List<String> openCommands,
        List<Button> buttons
) {
}
