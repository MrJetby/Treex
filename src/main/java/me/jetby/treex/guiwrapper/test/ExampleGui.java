package me.jetby.treex.guiwrapper.test;

import me.jetby.treex.guiwrapper.XGui;
import me.jetby.treex.guiwrapper.itemwrapper.LegacyWrapper;
import org.bukkit.Material;

public class ExampleGui extends XGui {


    private int current = 0;

    public ExampleGui() {
        super("");

        registerItem(LegacyWrapper.builder(Material.LIME_DYE).build());

        updateItem();
    }


    private void updateItem() {
        LegacyWrapper wrapper = LegacyWrapper.builder(Material.LIME_DYE)
                .slot(1)
                .displayName("---" + current + "---")
                .enchanted(true)
                .onClick(event -> {
                    event.setCancelled(true);
                    current++;
                    updateItem();
                })
                .build();

        registerItem(wrapper);
    }
}
