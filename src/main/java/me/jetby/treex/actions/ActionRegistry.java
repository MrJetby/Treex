package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.Treex;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@UtilityClass
public class ActionRegistry {
    private final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");
    private static final Logger logger = LogInitialize.getLogger(Treex.class);

    public List<RegistryActionEntry> transform(List<String> settings) {
        List<RegistryActionEntry> actions = new ArrayList<>();
        for (String s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                logger.warn("Illegal action pattern " + s);
                continue;
            }

            var typeName = matcher.group(1).toUpperCase();
            ActionType type = ActionType.getType(typeName);
            Action action;

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
            actions.add(new RegistryActionEntry(action, context));
        }
        return actions;
    }

    public record RegistryActionEntry(Action action, String context) {}
}