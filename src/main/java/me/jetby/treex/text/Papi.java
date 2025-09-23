package me.jetby.treex.text;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.List;

@UtilityClass
public class Papi {

    public String setPapi(OfflinePlayer player, String text) {
        if (text == null) return null;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public List<String> setPapi(OfflinePlayer player, List<String> text) {
        if (text == null) return null;
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
