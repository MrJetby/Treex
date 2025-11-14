package me.jetby.treex.text;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class MiniMessageAmpersandSupport {
    public List<Component> component(List<String> input) {
        List<Component> list = new ArrayList<>();
        if (input == null || input.isEmpty()) return list;
        for (String string : input) {
            list.add(component(string));
        }
        return list;
    }

    public Component component(String input) {
        if (input == null || input.isEmpty()) return Component.empty();

        String parsed = "<!i>"+toMiniCompatible(input);
        return MiniMessage.miniMessage().deserialize(
                parsed
        );
    }

    private String toMiniCompatible(String input) {
        input = input.replaceAll("&#([0-9a-fA-F]{6})", "<#$1>");
        return input
                .replace("&0", "<black>")
                .replace("&1", "<dark_blue>")
                .replace("&2", "<dark_green>")
                .replace("&3", "<dark_aqua>")
                .replace("&4", "<dark_red>")
                .replace("&5", "<dark_purple>")
                .replace("&6", "<gold>")
                .replace("&7", "<gray>")
                .replace("&8", "<dark_gray>")
                .replace("&9", "<blue>")
                .replace("&a", "<green>")
                .replace("&b", "<aqua>")
                .replace("&c", "<red>")
                .replace("&d", "<light_purple>")
                .replace("&e", "<yellow>")
                .replace("&f", "<white>")
                .replace("&l", "<bold>")
                .replace("&n", "<underlined>")
                .replace("&o", "<italic>")
                .replace("&m", "<strikethrough>")
                .replace("&k", "<obfuscated>")
                .replace("&r", "<reset>");
    }
}
