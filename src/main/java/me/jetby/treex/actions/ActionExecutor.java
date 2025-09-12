package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.Treex;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

@UtilityClass
public class ActionExecutor {

    public void execute(@NotNull ActionContext ctx, @NotNull Map<String, List<String>> actions) {
        List<Entry<String, List<String>>> actionList = new ArrayList<>(actions.entrySet());
        executeSequential(ctx, actionList, 0);
    }

    private void executeSequential(@NotNull ActionContext ctx, @NotNull List<Entry<String, List<String>>> actionList, long initialDelayTicks) {
        if (actionList.isEmpty()) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                processNextGroup(ctx, actionList, 0);
            }
        }.runTaskLater(Treex.getInstance(), initialDelayTicks);
    }

    private void processNextGroup(@NotNull ActionContext ctx, @NotNull List<Entry<String, List<String>>> actionList, int currentIndex) {
        if (currentIndex >= actionList.size()) return;

        Entry<String, List<String>> currentEntry = actionList.get(currentIndex);
        String actionName = currentEntry.getKey();
        List<String> contexts = currentEntry.getValue();

        Action action;
        ActionType type = ActionType.getType(actionName);
        if (type != null) {
            action = type.getAction();
        } else {
            action = ActionTypeRegistry.get(actionName);
        }

        if (action == null) {
            processNextGroup(ctx, actionList, currentIndex + 1);
            return;
        }

        if ("DELAY".equalsIgnoreCase(actionName)) {
            String delayStr = contexts.isEmpty() ? "0" : contexts.get(0);
            try {
                int delayTicks = Integer.parseInt(delayStr.trim());
                if (delayTicks > 0) {
                    executeSequential(ctx, actionList.subList(currentIndex + 1, actionList.size()), delayTicks);
                    return;
                }
            } catch (NumberFormatException e) {
                //
            }
        }

        for (String message : contexts) {
            ctx.put("message", message);
            action.execute(ctx);
        }

        processNextGroup(ctx, actionList, currentIndex + 1);
    }
}