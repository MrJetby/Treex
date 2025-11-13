package me.jetby.treex.guiwrapper.test;

import me.jetby.treex.guiwrapper.XGui;
import me.jetby.treex.guiwrapper.itemwrapper.ItemWrapper;
import me.jetby.treex.guiwrapper.itemwrapper.LegacyWrapper;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;

public class ExampleGui extends XGui {


    private int current = 0;

    public ExampleGui() {
        super(InventoryType.CHEST);

        registerItem(1, LegacyWrapper.builder(Material.LIME_DYE).build());

        updateItem();
    }


    private void updateItem() {
        LegacyWrapper wrapper = LegacyWrapper.builder(Material.LIME_DYE)
                .displayName("---" + current + "---")
                .enchanted(true)
                .onClick(event -> {
                    event.setCancelled(true);
                    current++;
                    updateItem();
                })
                .build();

        registerItem(5, wrapper);
    }
}
