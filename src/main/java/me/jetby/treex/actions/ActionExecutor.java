package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.Treex;
import me.jetby.treex.actions.impl.standard.Delay;
import me.jetby.treex.actions.impl.standard.MiniMessage;
import me.jetby.treex.text.Colorize;
import me.jetby.treex.text.Papi;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@UtilityClass
public class ActionExecutor {

    public void execute(@NotNull ActionContext ctx,
                        @NotNull List<ActionRegistry.RegistryActionEntry> actions) {
        executeSequential(ctx, actions, 0);
    }

    private void executeSequential(@NotNull ActionContext ctx,
                                   List<ActionRegistry.RegistryActionEntry> actions,
                                   int startIndex) {

        for (int i = startIndex; i < actions.size(); i++) {

            ActionRegistry.RegistryActionEntry entry = actions.get(i);
            Action action = entry.action();
            String c = Papi.setPapi(ctx.getPlayer(), entry.context());
            if (!(action instanceof MiniMessage)) {
                c = Colorize.text(entry.context());
            }

            ctx.put("message", c);
            ctx.put("context", c);

            if (action instanceof Delay) {
                try {
                    int delayTicks = Integer.parseInt(c);
                    int finalI = i;
                    Bukkit.getScheduler().runTaskLater(Treex.getInstance(), () ->
                            executeSequential(ctx, actions, finalI + 1), delayTicks);
                    return;
                } catch (NumberFormatException e) {
                    continue;
                }
            } else {
                action.execute(ctx);
            }
        }
    }
}