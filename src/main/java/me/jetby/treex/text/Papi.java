package me.jetby.treex.text;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class Papi {

    public String setPapi(Player player, String text) {
        if (text == null) return null;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public List<String> setPapi(Player player, List<String> text) {
        if (text == null) return null;
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
