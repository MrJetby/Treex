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

    public Map<String, List<String>> transform(List<String> settings) {
        Map<String, List<String>> actions = new LinkedHashMap<>();

        for (String s : settings) {
            var matcher = ACTION_PATTERN.matcher(s);
            if (!matcher.matches()) {
                logger.warn("Illegal action pattern " + s);
                continue;
            }

            String actionName = matcher.group(1).toUpperCase();
            String context = matcher.group(2).trim();

            actions.putIfAbsent(actionName, new ArrayList<>());
            actions.get(actionName).add(context);
        }

        return actions;
    }
}
