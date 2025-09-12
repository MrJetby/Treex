package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.text.Colorize;
import me.jetby.treex.text.Papi;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ActionExecutor {

    public void execute(@NotNull ActionContext ctx,
                        @NotNull Map<ActionType, List<String>> actions) {

        for (var entry : actions.entrySet()) {
            ActionType type = entry.getKey();
            List<String> contexts = entry.getValue();

            for (String message : contexts) {
                ctx.put("message", message);

                var action = type.getAction();
                if (action != null) {
                    action.execute(ctx);
                } else {
                    var custom = ActionTypeRegistry.get(type.name());
                    if (custom != null) custom.execute(ctx);
                }
            }
        }
    }
}
