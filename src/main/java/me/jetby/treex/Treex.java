package me.jetby.treex;

import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;
import me.jetby.treex.worldguard.WGHook;
import org.bukkit.plugin.java.JavaPlugin;


public final class Treex extends JavaPlugin {

    private static Treex INSTANCE;
    public static Treex getInstance() {
        return INSTANCE;
    }
    public static final Logger LOGGER = LogInitialize.getLogger("EvilMobs");

    @Override
    public void onEnable() {
        INSTANCE = this;

        WGHook.init();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static void init(JavaPlugin plugin) {
        LOGGER.success(plugin.getName() + " has been registered");
    }

}
