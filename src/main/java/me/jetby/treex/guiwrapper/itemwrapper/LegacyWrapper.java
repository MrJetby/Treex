package me.jetby.treex.guiwrapper.itemwrapper;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class LegacyWrapper implements ItemWrapper {

    private int slot;
    private ItemStack itemStack;
    private Material material;
    private String displayName;
    private List<String> lore;
    private int customModelData;
    private boolean enchanted;
    private List<ItemFlag> flags;
    private Consumer<InventoryClickEvent> onClick;

    public LegacyWrapper(@NotNull final ItemStack itemStack) {
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

    public final String displayName() {
        return displayName;
    }

    public final void displayName(String displayName) {
        this.displayName = displayName;
    }

    public final List<String> lore() {
        return lore;
    }

    public final void lore(List<String> lore) {
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

    public static LegacyWrapper.Builder builder(@NotNull Material material) {
        return new Builder(material);
    }

    public static class Builder {
        private ItemStack itemStack;
        private Material material;
        private String displayName;
        private List<String> lore;
        private int customModelData;
        private boolean enchanted;
        private List<ItemFlag> flags;
        private Consumer<InventoryClickEvent> onClick;

        private Builder(@NotNull Material material) {
            this.material = material;
        }
        public LegacyWrapper.Builder onClick(Consumer<InventoryClickEvent> onClick) {
            this.onClick = onClick;
            return this;
        }
        public final void flags(List<ItemFlag> flags) {
            this.flags = flags;
        }
        public LegacyWrapper.Builder itemStack(ItemStack itemStack) {
            this.itemStack = itemStack;
            return this;
        }
        public LegacyWrapper.Builder material(Material material) {
            this.material = material;
            return this;
        }
        public LegacyWrapper.Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }
        public LegacyWrapper.Builder lore(List<String> lore) {
            this.lore = lore;
            return this;
        }
        public LegacyWrapper.Builder customModelData(int customModelData) {
            this.customModelData = customModelData;
            return this;
        }
        public LegacyWrapper.Builder enchanted(boolean enchanted) {
            this.enchanted = enchanted;
            return this;
        }
        public LegacyWrapper build() {
            LegacyWrapper wrapper = new LegacyWrapper(itemStack);

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
