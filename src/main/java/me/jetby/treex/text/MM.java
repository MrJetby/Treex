package me.jetby.treex.text;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.ArrayList;
import java.util.List;


@UtilityClass
public class MM {

    private final MiniMessage MINI = MiniMessage.miniMessage();

    public List<Component> component(List<String> input) {
        List<Component> list = new ArrayList<>();
        for (String string : input) {
            list.add(component(string));
        }
        return list;
    }

    public Component component(String input) {
        if (input == null || input.isEmpty()) return Component.empty();

        String parsed = toMiniCompatible(input);


        return MINI.deserialize(parsed);
    }

    private static String toMiniCompatible(String input) {
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
