package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class ActionTypeRegistry {

    private final Map<String, Action> CUSTOM_ACTIONS = new ConcurrentHashMap<>();

    public void register(String name, Action action) {
        CUSTOM_ACTIONS.put(name.toUpperCase(), action);
    }

    public Action get(String name) {
        ActionType standard = ActionType.getType(name);
        if (standard != null) return standard.getAction();

        return CUSTOM_ACTIONS.get(name.toUpperCase());
    }
}
