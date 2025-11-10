package me.jetby.treex.gui.test;

import me.jetby.treex.gui.itemwrapper.LegacyWrapper;
import me.jetby.treex.gui.XGui;
import me.jetby.treex.text.MM;
import me.jetby.treex.text.Papi;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;

import java.util.List;

public class ExampleGui extends XGui {


    public ExampleGui(Player player) {
        super(InventoryType.CHEST);

        registerItem(5, LegacyWrapper.builder(Material.LIME_DYE)
                .displayName(Papi.set(player, "<!i><#88FB82>%vault_eco_balance_fixed%"))
                .lore(Papi.set(player, List.of("&6balance: &e%vault_eco_balance_fixed%")))
                .enchanted(true)
                .onClick(event -> {
                    event.setCancelled(true);
                    player.sendMessage(MM.component("<#FF0000>Ты кликнул на зелёный предмет!"));
                    player.sendMessage(MM.component("<#FF0000>Ты кликнул на зелёный предмет!<#0000FF>"));
                    player.sendMessage(MM.component("#FF0000Ты кликнул на зелёный предмет!"));
                    player.sendMessage(MM.component("&6&lТы кликнул на зелёный &eпредмет!"));
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "eco take "+player.getName()+" 100");
                        redraw(5);
                })
                .build());
    }


    @Override
    protected void setBase() {

    }
}
