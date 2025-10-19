package me.jetby.treex;

import me.jetby.treex.events.TreexOnPluginDisable;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;
import me.jetby.treex.worldguard.WGHook;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public final class Treex extends JavaPlugin {

    private static Treex INSTANCE;
    public static Treex getInstance() {
        return INSTANCE;
    }
    public static final Logger LOGGER = LogInitialize.getLogger("Treex");

    private boolean isPlaceholderApiHooked = false;
    @Override
    public void onEnable() {
        INSTANCE = this;

        isPlaceholderApiHooked = getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
        if (getServer().getPluginManager().getPlugin("WorldEdit")==null) {
            LOGGER.error("WorldEdit was not found");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        if (getServer().getPluginManager().getPlugin("WorldGuard")==null) {
            LOGGER.error("WorldGuard was not found");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        WGHook.init();

    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().callEvent(new TreexOnPluginDisable());

    }

    public boolean isPlaceholderApiHooked() {
        return isPlaceholderApiHooked;
    }

    public static void init(JavaPlugin plugin) {
        LOGGER.success(plugin.getName() + " has been registered");
    }

}
