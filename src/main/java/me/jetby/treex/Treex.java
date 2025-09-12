package me.jetby.treex;

import org.bukkit.plugin.java.JavaPlugin;

public final class Treex extends JavaPlugin {

    private static Treex INSTANCE;
    public static Treex getInstance() {
        return INSTANCE;
    }

    @Override
    public void onEnable() {
        INSTANCE = this;

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
