package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import org.jetbrains.annotations.NotNull;


public class Title implements Action {
    @Override
    public void execute(@NotNull ActionContext context) {

        if (context.getPlayer() == null) return;

        String message = context.get("message", String.class);
        if (message == null) return;

        var args = message.split(";");
        var title = args.length > 0 ? args[0] : "";
        var subTitle = args.length > 1 ? args[1] : "";
        int fadeIn = (args.length > 2 ? Integer.parseInt(args[2]) : 10) * 50;
        int stayIn = (args.length > 3 ? Integer.parseInt(args[3]) : 70) * 50;
        int fadeOut = (args.length > 4 ? Integer.parseInt(args[4]) : 20) * 50;

        context.getPlayer().sendTitle(title, subTitle, fadeIn, stayIn, fadeOut);

    }
}
