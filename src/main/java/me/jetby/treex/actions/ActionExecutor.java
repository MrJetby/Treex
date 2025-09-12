package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.Treex;
import me.jetby.treex.text.Colorize;
import me.jetby.treex.text.Papi;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class ActionExecutor {

    public void execute(@NotNull ActionContext ctx,
                        @NotNull List<ActionRegistry.ActionEntry> actions) {
        executeSequential(ctx, actions, 0);
    }

    private void executeSequential(@NotNull ActionContext ctx,
                                   List<ActionRegistry.ActionEntry> actions,
                                   int startIndex) {

        for (int i = startIndex; i < actions.size(); i++) {

            ActionRegistry.ActionEntry entry = actions.get(i);
            ActionType type = entry.type();
            String c = Papi.setPapi(ctx.getPlayer(), Colorize.text(entry.context()));

            ctx.put("message", c);

            if (type == ActionType.DELAY) {
                try {
                    int delayTicks = Integer.parseInt(c);
                    int finalI = i;
                    Bukkit.getScheduler().runTaskLater(Treex.getInstance(), () ->
                            executeSequential(ctx, actions, finalI), delayTicks);

                    return;
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                type.getAction().execute(ctx);
            }

        }
    }
}