package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.Treex;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@UtilityClass
public class ActionRegistry {
    private final Pattern ACTION_PATTERN = Pattern.compile("\\[(\\S+)] ?(.*)");

    private static final Logger logger = LogInitialize.getLogger(Treex.class);

    public Map<ActionType, List<String>> transform(List<String> settings) {
        Map<ActionType, List<String>> actions = new HashMap<>();
        for (String s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                logger.warn("Illegal action pattern " + s);
                continue;
            }

            var type = ActionType.getType(matcher.group(1).toUpperCase());
            if (type == null) {
                logger.warn("ActionType " + s + " is not available!");
                continue;
            }

            var context = matcher.group(2).trim();
            actions.putIfAbsent(type, new ArrayList<>());
            actions.get(type).add(context);
        }
        return actions;
    }
}
