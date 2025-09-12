package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.Treex;
import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;
import org.jetbrains.annotations.NotNull;

public class Sound implements Action {
    private static final Logger logger = LogInitialize.getLogger(Treex.class);
    @Override
    public void execute(@NotNull ActionContext context) {

        String message = context.get("message", String.class);
        if (message == null) return;
        if (context.getPlayer() == null) return;

        var args = message.split(";");
        org.bukkit.Sound sound;
        try {
            if (args.length >= 1) {
                sound = org.bukkit.Sound.valueOf(args[0].toUpperCase());
            } else {
                logger.warn("Sound is null");
                return;
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Sound " + args[0] + " is not available");
            return;
        }

        try {
            float volume = args.length > 1 ? Float.parseFloat(args[1]) : 1;
            float pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1;
            context.getPlayer().playSound(context.getPlayer().getLocation(), sound, volume, pitch);
        } catch (NumberFormatException e) {
            logger.warn("Volume and pitch must be a number");
        }
    }
}