package me.jetby.treex.actions;

import lombok.experimental.UtilityClass;
import me.jetby.treex.text.Colorize;
import me.jetby.treex.text.Papi;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Map;

@UtilityClass
public class ActionExecutor {

    public void execute(Player player,
                        Map<ActionType, List<String>> actions
    ) {
        actions.forEach((type, contexts) -> {
            for (String raw : contexts) {
                String parsed = Papi.setPapi(player, Colorize.text(raw));

                ActionContext ctx = new ActionContext(player);
                ctx.put("message", parsed);

                type.getAction().execute(ctx);
            }
        });
    }
}
