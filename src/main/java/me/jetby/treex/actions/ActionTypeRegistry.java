package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class ActionTypeRegistry {

    private final Map<String, ActionEntry> CUSTOM_ACTIONS = new ConcurrentHashMap<>();

    public void register(String name, Action action) {
        CUSTOM_ACTIONS.put(name.toUpperCase(), new ActionEntry(null, name, action));
    }

    public void register(Set<ActionEntry> entries) {
        for (ActionEntry entry : entries) {
            CUSTOM_ACTIONS.put(entry.name().toUpperCase(), new ActionEntry(null, entry.name(), entry.action()));
        }
    }

    public void register(String author, String name, Action action) {
        CUSTOM_ACTIONS.put(name.toUpperCase(), new ActionEntry(author.toLowerCase(), name, action));
    }

    public void register(String author, Set<ActionEntry> entries) {
        for (ActionEntry entry : entries) {
            CUSTOM_ACTIONS.put(entry.name().toUpperCase(), new ActionEntry(author.toLowerCase(), entry.name(), entry.action()));
        }
    }

    public void unregister(String name) {
        CUSTOM_ACTIONS.remove(name.toUpperCase());
    }
    public void unregisterAll(String author) {
        CUSTOM_ACTIONS.remove(author.toLowerCase());
    }

    public Action get(String name) {
        ActionType standard = ActionType.getType(name);
        if (standard != null) return standard.getAction();

        return CUSTOM_ACTIONS.get(name.toUpperCase()).action();
    }

}
