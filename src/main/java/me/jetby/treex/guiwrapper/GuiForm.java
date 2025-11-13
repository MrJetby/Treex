package me.jetby.treex.guiwrapper;

import lombok.Getter;
import me.jetby.treex.gui.Button;
import me.jetby.treex.gui.ButtonCommand;
import me.jetby.treex.gui.Menu;
import me.jetby.treex.gui.SkullCreator;
import me.jetby.treex.gui.requirements.ClickRequirement;
import me.jetby.treex.gui.requirements.ViewRequirement;
import me.jetby.treex.text.Papi;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static me.jetby.treex.Treex.LOGGER;

public class GuiForm {

    @Getter
    private Menu menu;

    public GuiBuilder menuLoad(@NotNull FileConfiguration configuration,
                               @Nullable Consumer<GuiBuilder> builderConsumer) {

        try {
            String menuId = configuration.getString("id");
            String title = configuration.getString("title");
            int size = configuration.getInt("size", 3);
            if (size < 9) {
                if (size <= 6 && size>0) {
                    size = size * 9;
                } else {
                    size = 9;
                }
            }
            String permission = configuration.getString("open_permission");
            InventoryType inventoryType = InventoryType.valueOf(configuration.getString("inventory", "CHEST"));
            List<String> openCommands = configuration.getStringList("open_commands");

            Map<String, Object> customValues = new HashMap<>();
            GuiBuilder builder = new GuiBuilder(customValues);
            if (builderConsumer != null) {
                builderConsumer.accept(builder);
            }

            List<Button> buttons = getButtons(configuration, customValues);

            menu = new Menu(menuId, title, size, inventoryType, permission, openCommands, buttons);
            return builder;
        } catch (Exception e) {
            LOGGER.error("Error parsing menu: " + e.getMessage());
            return null;
        }
    }

    public ButtonBuilder buttonLoad(FileConfiguration configuration, @Nullable Consumer<ButtonBuilder> builderConsumer) {
        Map<String, Object> customValues = new HashMap<>();
        ButtonBuilder builder = new ButtonBuilder(configuration, customValues);
        if (builderConsumer != null) {
            builderConsumer.accept(builder);
        }
        getButtons(configuration, customValues);
        return builder;
    }

    private List<Button> getButtons(FileConfiguration config, Map<String, Object> customValues) {
        List<Button> buttons = new ArrayList<>();
        ConfigurationSection itemsSection = config.getConfigurationSection("Items");
        if (itemsSection != null) {
            for (String key : itemsSection.getKeys(false)) {
                ConfigurationSection itemSection = itemsSection.getConfigurationSection(key);
                if (itemSection != null) {
                    String displayName = itemSection.getString("display_name");
                    List<String> lore = itemSection.getStringList("lore");
                    List<Integer> slots = parseSlots(itemSection.get("slot"));
                    int amount = itemSection.getInt("amount", 1);
                    int customModelData = itemSection.getInt("custom-model-data", 0);
                    boolean enchanted = itemSection.getBoolean("enchanted", false);
                    boolean freeSlot = itemSection.getBoolean("free-slot", false);
                    boolean hideAttributes = itemSection.getBoolean("hide_attributes", false);
                    boolean hideEnchantments = itemSection.getBoolean("hide_enchantments", false);
                    int priority = itemSection.getInt("priority", 0);
                    String type = itemSection.getString("type", "default");
                    String defaultMaterial = freeSlot ? "AIR" : "STONE";
                    String rgb = itemSection.getString("color", "WHITE");
                    String material = Papi.setPapi(null, itemSection.getString("material", defaultMaterial));
                    ItemStack itemStack;
                    if (material.startsWith("basehead-")) {
                        try {
                            itemStack = SkullCreator.itemFromBase64(material.replace("basehead-", ""));
                        } catch (Exception e) {
                            Bukkit.getLogger().warning("Error creating custom skull: " + e.getMessage());
                            itemStack = new ItemStack(SkullCreator.createSkull());
                        }
                    } else {
                        itemStack = new ItemStack(Material.valueOf(material));
                    }
                    itemStack.setAmount(amount);
                    ItemMeta meta = itemStack.getItemMeta();
                    if (meta != null) {
                        if (hideAttributes) meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                        meta.addItemFlags(ItemFlag.HIDE_DYE);
                        meta.setDisplayName(displayName);
                        meta.setLore(lore);
                        meta.setCustomModelData(customModelData);
                        if (meta instanceof LeatherArmorMeta lam) {
                            lam.setColor(getColorByName(rgb));
                        }
                        if (meta instanceof PotionMeta potionMeta) {
                            potionMeta.setColor(getColorByName(rgb));
                        }
                        itemStack.setItemMeta(meta);
                    }

                    for (Integer slot : slots) {
                        buttons.add(new Button(key, slot, displayName,
                                lore, priority, amount, customModelData,
                                enchanted, freeSlot, hideAttributes, hideEnchantments,
                                itemStack, parseViewRequirements(itemSection, slot),
                                parseClickCommands(itemSection),
                                type,
                                customValues
                        ));
                    }
                }
            }
        }
        return buttons;
    }

    private List<ButtonCommand> parseClickCommands(ConfigurationSection itemSection) {
        List<ButtonCommand> buttonCommands = new ArrayList<>();
        if (itemSection.contains("left_click_commands")) {
            buttonCommands.add(new ButtonCommand(false, ClickType.LEFT, itemSection.getStringList("left_click_commands"),
                    parseClickRequirements(itemSection, "left_click_requirements", ClickType.LEFT, false)));
        }
        if (itemSection.contains("right_click_commands")) {
            buttonCommands.add(new ButtonCommand(false, ClickType.RIGHT, itemSection.getStringList("right_click_commands"),
                    parseClickRequirements(itemSection, "right_click_requirements", ClickType.RIGHT, false)));
        }
        if (itemSection.contains("shift_left_click_commands")) {
            buttonCommands.add(new ButtonCommand(false, ClickType.SHIFT_LEFT, itemSection.getStringList("shift_left_click_commands"),
                    parseClickRequirements(itemSection, "shift_left_click_requirements", ClickType.SHIFT_LEFT, false)));
        }
        if (itemSection.contains("shift_right_click_commands")) {
            buttonCommands.add(new ButtonCommand(false, ClickType.SHIFT_RIGHT, itemSection.getStringList("shift_right_click_commands"),
                    parseClickRequirements(itemSection, "shift_right_click_requirements", ClickType.SHIFT_RIGHT, false)));
        }
        if (itemSection.contains("click_commands")) {
            buttonCommands.add(new ButtonCommand(true, ClickType.UNKNOWN, itemSection.getStringList("click_commands"),
                    parseClickRequirements(itemSection, "click_requirements", ClickType.UNKNOWN, true)));
        }
        if (itemSection.contains("drop_commands")) {
            buttonCommands.add(new ButtonCommand(false, ClickType.DROP, itemSection.getStringList("drop_commands"),
                    parseClickRequirements(itemSection, "drop_requirements", ClickType.DROP, false)));
        }
        return buttonCommands;
    }

    private List<ViewRequirement> parseViewRequirements(ConfigurationSection itemSection, int slot) {
        List<ViewRequirement> requirements = new ArrayList<>();
        ConfigurationSection requirementsSection = itemSection.getConfigurationSection("view_requirements");
        if (requirementsSection == null) {
            return requirements;
        }
        for (String key : requirementsSection.getKeys(false)) {
            ConfigurationSection section = requirementsSection.getConfigurationSection(key);
            if (section == null) continue;
            String input = section.getString("input");
            String permission = section.getString("permission");
            requirements.add(new ViewRequirement(
                    section.getString("type"),
                    input,
                    section.getString("output"),
                    section.getBoolean("free-slot", false),
                    permission));
        }
        return requirements;
    }

    private List<ClickRequirement> parseClickRequirements(ConfigurationSection itemSection, String name, ClickType clickType, boolean anyClick) {
        List<ClickRequirement> requirements = new ArrayList<>();
        ConfigurationSection requirementsSection = itemSection.getConfigurationSection(name);
        if (requirementsSection == null) {
            return requirements;
        }
        for (String key : requirementsSection.getKeys(false)) {
            ConfigurationSection section = requirementsSection.getConfigurationSection(key);
            if (section == null) continue;
            requirements.add(new ClickRequirement(anyClick, clickType,
                    section.getString("type"),
                    section.getString("input"),
                    section.getString("output"),
                    section.getString("permission"),
                    section.getStringList("deny_commands")));
        }
        return requirements;
    }

    private List<Integer> parseSlots(Object slotObject) {
        List<Integer> slots = new ArrayList<>();
        if (slotObject instanceof Integer) {
            slots.add((Integer) slotObject);
        } else if (slotObject instanceof String) {
            String slotString = ((String) slotObject).trim();
            slots.addAll(parseSlotString(slotString));
        } else if (slotObject instanceof List<?>) {
            for (Object obj : (List<?>) slotObject) {
                if (obj instanceof Integer) {
                    slots.add((Integer) obj);
                } else if (obj instanceof String) {
                    slots.addAll(parseSlotString((String) obj));
                }
            }
        } else {
            Bukkit.getLogger().warning("Unknown slot format: " + slotObject);
        }
        return slots;
    }

    private List<Integer> parseSlotString(String slotString) {
        List<Integer> slots = new ArrayList<>();
        if (slotString.contains("-")) {
            try {
                String[] range = slotString.split("-");
                int start = Integer.parseInt(range[0].trim());
                int end = Integer.parseInt(range[1].trim());
                for (int i = start; i <= end; i++) {
                    slots.add(i);
                }
            } catch (NumberFormatException e) {
                Bukkit.getLogger().warning("Error parsing slot range: " + slotString);
            }
        } else {
            try {
                slots.add(Integer.parseInt(slotString));
            } catch (NumberFormatException e) {
                Bukkit.getLogger().warning("Error parsing single slot: " + slotString);
            }
        }
        return slots;
    }

    public record GuiBuilder(@NotNull Map<String, Object> customValues) {
        public void set(String key, Object value) {
            customValues.put(key, value);
        }

        public Object get(String key) {
            return customValues.get(key);
        }
    }

    public record ButtonBuilder(@NotNull ConfigurationSection config, @NotNull Map<String, Object> customValues) {
        public void set(String key, Object value) {
            customValues.put(key, value);
        }
        public Object get(String key) {
            return customValues.get(key);
        }
    }

    private static @NotNull Color getColorByName(@NotNull String name) {
        try {
            Field field = Color.class.getDeclaredField(name.toUpperCase());

            Object o = field.get(null);
            if (o instanceof Color color) {
                return color;
            }
        } catch (Exception ignored) {
        }

        return Color.WHITE;
    }

}