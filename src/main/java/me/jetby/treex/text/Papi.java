package me.jetby.treex.text;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import me.jetby.treex.Treex;
import org.bukkit.OfflinePlayer;

import java.util.List;

@UtilityClass
public class Papi {

    public String set(OfflinePlayer player, String text) {
        return setPapi(player, text);
    }

    public List<String> set(OfflinePlayer player, List<String> text) {
        return setPapi(player, text);
    }

    public String setPapi(OfflinePlayer player, String text) {
        if (text == null) return null;
        if (!Treex.getInstance().isPlaceholderApiHooked()) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }

    public List<String> setPapi(OfflinePlayer player, List<String> text) {
        if (text == null) return null;
        if (!Treex.getInstance().isPlaceholderApiHooked()) return text;
        return PlaceholderAPI.setPlaceholders(player, text);
    }
}
