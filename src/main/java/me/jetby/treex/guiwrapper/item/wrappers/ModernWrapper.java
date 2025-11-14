package me.jetby.treex.guiwrapper.item.wrappers;

import me.jetby.treex.guiwrapper.item.ItemWrapper;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ModernWrapper implements ItemWrapper {


    private int slot;
    private ItemStack itemStack;
    private Material material;
    private Component displayName;
    private List<Component> lore;
    private int customModelData;
    private boolean enchanted;
    private List<ItemFlag> flags;
    private Consumer<InventoryClickEvent> onClick;

    public ModernWrapper(@NotNull final ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    public final Consumer<InventoryClickEvent> onClick() {
        return onClick;
    }
    public final void onClick(Consumer<InventoryClickEvent> onClick) {
        this.onClick = onClick;
    }
    public final int slot() {
        return slot;
    }
    public final void slot(int slot) {
        this.slot = slot;
    }
    public final List<ItemFlag> flags() {
        return flags;
    }
    public final void flags(ItemFlag... flags) {
        this.flags = Arrays.asList(flags);
    }

    public final ItemStack itemStack() {
        return itemStack;
    }

    public final void itemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public final Material material() {
        return material;
    }

    public final void material(Material material) {
        this.material = material;
    }

    public final Component displayName() {
        return displayName;
    }

    public final void displayName(Component displayName) {
        this.displayName = displayName;
    }

    public final List<Component> lore() {
        return lore;
    }

    public final void lore(List<Component> lore) {
        this.lore = lore;
    }

    public final int customModelData() {
        return customModelData;
    }

    public final void customModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public final boolean enchanted() {
        return enchanted;
    }

    public final void enchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }

    public static ModernWrapper.Builder builder(@NotNull Material material) {
        return new ModernWrapper.Builder(material);
    }

    public static class Builder {
        private ItemStack itemStack;
        private Material material;
        private Component displayName;
        private List<Component> lore;
        private int customModelData;
        private boolean enchanted;
        private List<ItemFlag> flags;
        private Consumer<InventoryClickEvent> onClick;

        private Builder(@NotNull Material material) {
            this.material = material;
        }
        public ModernWrapper.Builder onClick(Consumer<InventoryClickEvent> onClick) {
            this.onClick = onClick;
            return this;
        }
        public final void flags(List<ItemFlag> flags) {
            this.flags = flags;
        }
        public ModernWrapper.Builder itemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }
        public ModernWrapper.Builder material(Material material) {
            this.material = material;
            return this;
        }
        public ModernWrapper.Builder displayName(Component displayName) {
            this.displayName = displayName;
            return this;
        }
        public ModernWrapper.Builder lore(List<Component> lore) {
            this.lore = lore;
            return this;
        }
        public ModernWrapper.Builder customModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }
        public ModernWrapper.Builder enchanted(boolean enchanted) {
            this.enchanted = enchanted;
            return this;
        }
        public ModernWrapper build() {
            ModernWrapper wrapper = new ModernWrapper(itemStack);

            wrapper.displayName = displayName;
            wrapper.lore = lore;
            wrapper.customModelData = customModelData;
            wrapper.enchanted = enchanted;
            wrapper.material = material;
            wrapper.itemStack = itemStack;
            wrapper.flags = flags;
            wrapper.onClick = onClick;

            return wrapper;
        }


    }
}