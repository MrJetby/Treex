package me.jetby.treex.bukkit;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

@UtilityClass
public class LocationHandler {

    public String serialize(Location loc) {
        return String.format("%d;%d;%d;%s", loc.getBlockX(), loc.getBlockY(), loc.getBlockZ(), loc.getWorld().getName());

    }

    public Location deserialize(String str) {
        if (str == null || str.isEmpty() || str.equals("0;0;0;world")) {
            return null;
        }

        try {
            String[] parts = str.split(";");
            if (parts.length < 4) return null;

            double x = Double.parseDouble(parts[0]);
            double y = Double.parseDouble(parts[1]);
            double z = Double.parseDouble(parts[2]);
            World world = Bukkit.getWorld(parts[3]);

            return world != null ? new Location(world, x, y, z) : null;
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return null;
        }
    }
}
