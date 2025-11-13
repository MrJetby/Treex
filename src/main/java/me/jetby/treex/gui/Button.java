package me.jetby.treex.gui;

import me.jetby.treex.gui.requirements.ViewRequirement;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public record Button(

        String id,
        int slot,
        String displayName,
        List<String> lore,
        int priority,
        int amount,
        int customModelData,
        boolean enchanted,
        boolean freeSlot,
        boolean hideAttributes,
        boolean hideEnchantments,
        ItemStack itemStack,
        List<ViewRequirement> viewRequirements,
        List<ButtonCommand> buttonCommands,
        String type,
        Map<String, Object> customValues


) {
}
