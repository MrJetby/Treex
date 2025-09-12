package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import net.md_5.bungee.api.ChatMessageType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastActionBar implements Action {
    @Override
    public void execute(@NotNull ActionContext context) {

        String message = context.get("message", String.class);
        if (message != null && !message.isEmpty()) {
            for (Player p : Bukkit.getOnlinePlayers()) {

                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, net.md_5.bungee.api.chat.TextComponent.fromLegacyText(message));
            }
        }
    }
}
