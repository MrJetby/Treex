package me.jetby.treex.tools;

import lombok.experimental.UtilityClass;
import me.jetby.treex.tools.log.Logger;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class LogInitialize {

    private final Map<String, Logger> cache = new ConcurrentHashMap<>();

    public Logger getLogger(Class<?> clazz) {
        Plugin plugin = JavaPlugin.getProvidingPlugin(clazz);
        return getLogger(plugin);
    }

    public Logger getLogger(Plugin plugin) {
        return getLogger(plugin.getName());
    }

    public Logger getLogger(String pluginName) {
        return cache.computeIfAbsent(pluginName, name -> new Logger() {
            @Override
            public void msg(String message) {
                Bukkit.getConsoleSender().sendMessage("§6[" + name + "] §f" + message
                        .replace("&", "§"));
            }

            @Override
            public void warn(String message) {
                Bukkit.getConsoleSender().sendMessage("§e[" + name + "] §e" + message
                        .replace("&", "§"));
            }

            @Override
            public void info(String message) {
                Bukkit.getConsoleSender().sendMessage("§a[" + name + "] §f" + message
                        .replace("&", "§"));
            }

            @Override
            public void success(String message) {
                Bukkit.getConsoleSender().sendMessage("§a[" + name + "] §a" + message
                        .replace("&", "§"));
            }

            @Override
            public void error(String message) {
                Bukkit.getConsoleSender().sendMessage("§c[" + name + "] §c" + message
                        .replace("&", "§"));
            }
        });
    }
}
