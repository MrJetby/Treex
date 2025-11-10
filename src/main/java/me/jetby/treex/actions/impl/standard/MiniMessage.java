package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import me.jetby.treex.text.MM;
import org.jetbrains.annotations.NotNull;

public class MiniMessage implements Action {
    @Override
    public void execute(@NotNull ActionContext context) {

        String message = context.get("message", String.class);
        if (message == null) return;
        if (context.getPlayer() != null) context.getPlayer().sendMessage(MM.component(message));
    }
}