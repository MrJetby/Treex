package me.jetby.treex.gui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GuiHolder implements InventoryHolder {

    private final XGui gui;


    private final Inventory inventory;

    public GuiHolder(@NotNull XGui gui) {
        this(gui, null);
    }

    public GuiHolder(@NotNull XGui gui, @Nullable InventoryType type) {
        this.gui = gui;
        this.inventory = type != null ? Bukkit.createInventory(this, type) : Bukkit.createInventory(this, 9, "title");
    }


    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
