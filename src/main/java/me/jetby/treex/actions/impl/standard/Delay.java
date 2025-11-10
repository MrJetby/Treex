package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import org.jetbrains.annotations.NotNull;

public class Delay implements Action {
    @Override
    public void execute(@NotNull ActionContext context) {
        // No-op: Delay is handled in ActionExecutor
    }
}
