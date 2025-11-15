package me.jetby.treex.guiwrapper;

import me.jetby.treex.guiwrapper.item.ItemWrapper;
import me.jetby.treex.guiwrapper.item.wrappers.LegacyWrapper;
import me.jetby.treex.guiwrapper.item.wrappers.ModernWrapper;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginatedXGui extends XGui {

    private final List<ItemWrapper> content = new ArrayList<>();
    private int[] allowedSlots;
    private int currentPage = 0;

    public PaginatedXGui(@NotNull String title, int... allowedSlots) {
        super(title);
        this.allowedSlots = allowedSlots;
    }
    public PaginatedXGui(@NotNull String title, int size) {
        super(title, size);
    }
    public PaginatedXGui(@Nullable GuiForm form) {
        super(form);
    }
    public PaginatedXGui(@NotNull String title, InventoryType inventoryType) {
        super(title, inventoryType);
    }
    public PaginatedXGui(@NotNull String title) {
        super(title);
    }

    public void addItemToContent(ItemWrapper wrapper) {
        content.add(wrapper);
    }

    public void setAllowedSlots(int... slots) {
        this.allowedSlots = slots;
    }
    private void clearContentSlots() {
        for (int slot : allowedSlots) {
            unregisterItem(slot);
        }
    }

    public void openPage(int page) {
        currentPage = page;
        clearContentSlots();

        int perPage = allowedSlots.length;
        int from = currentPage * perPage;
        int to = Math.min(from + perPage, content.size());

        int index = 0;

        for (int i = from; i < to; i++) {
            int slot = allowedSlots[index++];

            ItemWrapper item = content.get(i);

            if (item instanceof LegacyWrapper legacy) {
                legacy.slot(slot);
            } else if (item instanceof ModernWrapper modern) {
                modern.slot(slot);
            }

            registerItem(item);
        }

        everyPageLogic();
    }

    protected void everyPageLogic() {}

    public void nextPage() {
        int perPage = allowedSlots.length;

        if ((currentPage + 1) * perPage < content.size()) {
            currentPage++;
            openPage(currentPage);
        }
    }

    public void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            openPage(currentPage);
        }
    }

    @Override
    public void open(@NotNull Player player) {
        super.open(player);
        openPage(0);
    }
}
