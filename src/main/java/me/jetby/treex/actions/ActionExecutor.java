package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ActionExecutor {

    public void execute(@NotNull ActionContext ctx,
                        @NotNull Map<String, List<String>> actions) {

        for (var entry : actions.entrySet()) {
            String actionName = entry.getKey();
            List<String> contexts = entry.getValue();

            Action action = null;
            ActionType type = ActionType.getType(actionName);
            if (type != null) action = type.getAction();

            if (action == null) {
                action = ActionTypeRegistry.get(actionName);
            }

            if (action == null) continue;

            for (String message : contexts) {
                ctx.put("message", message);
                action.execute(ctx);
            }
        }
    }
}
