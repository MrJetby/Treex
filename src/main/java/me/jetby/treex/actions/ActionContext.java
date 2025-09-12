package me.jetby.treex.actions;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ActionContext {
    @Getter
    final Player player;
    final Map<String, Object> data;

    public ActionContext(Player player) {
        this.player = player;
        this.data = new HashMap<>();
    }

    public <T> void put(String key, T value) {
        data.put(key, value);
    }

    public <T> T get(String key, Class<T> type) {
        return type.cast(data.get(key));
    }
}
