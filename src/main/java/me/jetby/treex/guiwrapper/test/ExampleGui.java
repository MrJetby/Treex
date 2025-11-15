package me.jetby.treex.guiwrapper.test;

import me.jetby.treex.guiwrapper.PaginatedXGui;
import me.jetby.treex.guiwrapper.item.wrappers.LegacyWrapper;
import org.bukkit.Material;

public class ExampleGui extends PaginatedXGui {

    public ExampleGui() {
        super("123", 45);

        setAllowedSlots(3, 5);

        for (int i = 0; i < 30; i++) {
            int index = i;
            addItemToContent(LegacyWrapper.builder(Material.PAPER)
                    .displayName("Элемент #" + index)
                    .onClick(e -> {
                        e.setCancelled(true);
                        player.sendMessage("Нажал на " + index);
                    })
                    .build()
            );
        }

        openPage(0);
    }


    @Override
    protected void everyPageLogic() {
        registerItem(
                LegacyWrapper.builder(Material.ARROW)
                        .slot(13)
                        .displayName("§e← Назад")
                        .onClick(e -> {
                            e.setCancelled(true);
                            prevPage();
                        })
                        .build()
        );
        registerItem(
                LegacyWrapper.builder(Material.ARROW)
                        .slot(15)
                        .displayName("§eВперёд →")
                        .onClick(e -> {
                            e.setCancelled(true);
                            nextPage();
                        })
                        .build()
        );
    }
}
