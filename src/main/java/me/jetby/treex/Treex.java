package me.jetby.treex;

import lombok.Getter;
import me.jetby.treex.events.TreexOnPluginDisable;
import me.jetby.treex.gui.GuiListener;
import me.jetby.treex.gui.test.ExampleGui;
import me.jetby.treex.tools.LogInitialize;
import me.jetby.treex.tools.log.Logger;
import me.jetby.treex.worldguard.WGHook;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;


@Getter
public final class Treex extends JavaPlugin implements CommandExecutor {

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
        this.getCommand("treex").setExecutor(this);
        getServer().getPluginManager().registerEvents(new GuiListener(), this);
    }

    @Override
    public void onDisable() {
        Bukkit.getPluginManager().callEvent(new TreexOnPluginDisable());

    }

    public static void init(JavaPlugin plugin) {
        LOGGER.success(plugin.getName() + " has been registered");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player player) {
            new ExampleGui(player)
                    .open(player);
        }
        return true;
    }
}
