package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.Treex;
import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BroadcastSound implements Action {
    private static final Logger logger = LogInitialize.getLogger(Treex.class);

    @Override
    public void execute(@NotNull ActionContext context) {

        String message = context.get("message", String.class);

        var args = message.split(";");
        Sound sound;

        try {
            if (args.length >= 1) {
                sound = Sound.valueOf(args[0].toUpperCase());
            } else {
                logger.warn("Sound is null");
                return;
            }
        } catch (IllegalArgumentException e) {
            logger.warn("Sound " + args[0] + " is not available");
            return;
        }

        float volume = 0;
        float pitch = 0;

        try {
            volume = args.length > 1 ? Float.parseFloat(args[1]) : 1;
            pitch = args.length > 2 ? Float.parseFloat(args[2]) : 1;
        } catch (NumberFormatException e) {
            logger.warn("Volume and pitch must be a number");
        }

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.playSound(p.getLocation(), sound, volume, pitch);
        }
    }
}
