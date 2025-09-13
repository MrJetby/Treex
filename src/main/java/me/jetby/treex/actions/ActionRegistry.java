package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.Treex;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;

import java.util.*;
import java.util.regex.Pattern;

@UtilityClass
public class ActionRegistry {
    private final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final Logger logger = LogInitialize.getLogger(Treex.class);

    public List<ActionEntry> transform(List<String> settings) {
        List<ActionEntry> actions = new ArrayList<>();
        for (String s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                logger.warn("Illegal action pattern " + s);
                continue;
            }

            var typeName = matcher.group(1).toUpperCase();
            ActionType type = ActionType.getType(typeName);
            Action action = null;

            if (type != null) {
                action = type.getAction();
            } else {
                action = ActionTypeRegistry.get(typeName);
                if (action == null) {
                    logger.warn("ActionType " + typeName + " is not available!");
                    continue;
                }
            }

            var context = matcher.group(2).trim();
            actions.add(new ActionEntry(action, context));
        }
        return actions;
    }

    public record ActionEntry(Action action, String context) {}
}