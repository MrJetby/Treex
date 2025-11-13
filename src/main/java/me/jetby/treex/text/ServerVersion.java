package me.jetby.treex.text;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public class ServerVersion {
    public String getVersion() {
        String packageName = Bukkit.getServer().getClass().getPackage().getName();
        return packageName.substring(packageName.lastIndexOf('.') + 1);
    }
}
