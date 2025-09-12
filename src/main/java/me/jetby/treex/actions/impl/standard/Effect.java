package me.jetby.treex.actions.impl.standard;

import me.jetby.treex.Treex;
import me.jetby.treex.actions.Action;
import me.jetby.treex.actions.ActionContext;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class Effect implements Action {
    private static final Logger logger = LogInitialize.getLogger(Treex.class);

    @Override
    public void execute(@NotNull ActionContext context) {

        String message = context.get("message", String.class);
        if (context.getPlayer() == null) return;

        var args = message.split(";");
        PotionEffectType potionEffectType;
        try {
            if (args.length >= 1) {
                potionEffectType = PotionEffectType.getByName(args[0].toUpperCase());
            } else {
                logger.warn("PotionEffectType is null");
                return;
            }
        } catch (IllegalArgumentException e) {
            logger.warn("PotionEffectType " + args[0] + " is not available");
            return;
        }

        try {
            int duration = args.length > 1 ? Integer.parseInt(args[1]) : 0;
            int strength = args.length > 2 ? Integer.parseInt(args[2]) : 1;
            context.getPlayer().addPotionEffect(new PotionEffect(potionEffectType, duration * 20, strength));
        } catch (NumberFormatException e) {
            logger.warn("Strength and duration must be a number");
        }
    }
}