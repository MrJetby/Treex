package me.jetby.treex.guiwrapper;

import me.jetby.treex.guiwrapper.itemwrapper.ItemWrapper;
import me.jetby.treex.guiwrapper.itemwrapper.LegacyWrapper;
import me.jetby.treex.guiwrapper.itemwrapper.ModernWrapper;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class PaginatedXGui extends XGui {

    private final List<ItemWrapper> content = new ArrayList<>();
    private final int[] allowedSlots;
    private int currentPage = 0;

    public PaginatedXGui(@NotNull String title, int... allowedSlots) {
        super(title);
        this.allowedSlots = allowedSlots;
    }

    public void addItemToContent(ItemWrapper wrapper) {
        content.add(wrapper);
    }

    private void clearContentSlots() {
        for (int slot : allowedSlots) {
            unregisterItem(slot);
        }
    }

    public void applyPage() {
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
            applyPage();
        }
    }

    public void prevPage() {
        if (currentPage > 0) {
            currentPage--;
            applyPage();
        }
    }

    @Override
    public void open(@NotNull Player player) {
        super.open(player);
        applyPage();
    }
}
