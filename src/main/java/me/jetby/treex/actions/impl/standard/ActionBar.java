package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ActionBar implements Action {

    @Override
    public void execute(@NotNull ActionContext context) {
        Player player = context.getPlayer();
        if (player == null) return;

        String message = context.get("message", String.class);
        if (message != null && !message.isEmpty()) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message));
        }
    }
}
