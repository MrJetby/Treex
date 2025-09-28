package me.jetby.treex.bukkit;

import lombok.experimental.UtilityClass;
import me.jetby.treex.tools.Randomizer;
import me.jetby.treex.worldguard.WGHook;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

@UtilityClass
public class LocationGenerator {
    
    @Nullable
    public Location getRandomLocation(World world, int minRadius, int maxRadius) {
        return getRandomLocation(world, minRadius, maxRadius, null, true, 0);
    }
    @Nullable
    public Location getRandomLocation(World world, int minRadius, int maxRadius, @Nullable Set<Material> materials, boolean blacklist ) {
        return getRandomLocation(world, minRadius, maxRadius, materials, blacklist, 0);
    }
    @Nullable
    public Location getRandomLocation(World world, int minRadius, int maxRadius, @Nullable Set<Material> materials, boolean blacklist, int regionRadius) {
        return getRandomLocation(world, minRadius, maxRadius, materials, blacklist, regionRadius, 15);
    }
    @Nullable
    public Location getRandomLocation(World world, int minRadius, int maxRadius, @Nullable Set<Material> materials, boolean blacklist, int regionRadius, int attempts) {

        if (world == null) {
            return null;
        }

        if (minRadius < 0) {
            minRadius = 0;
        }
        if (maxRadius <= minRadius) {
            maxRadius = minRadius + 1;
        }

        for (int attempt = 0; attempt < attempts; attempt++) {
            int radius = minRadius + Randomizer.randInteger(minRadius, maxRadius);

            if (radius <= 0) {
                radius = 1;
            }

            int x = Randomizer.rand(radius * 2 + 1) - radius;
            int z = Randomizer.rand(radius * 2 + 1) - radius;

            int y = world.getHighestBlockYAt(x, z);

            Material blockType = world.getBlockAt(x, y, z).getType();

            if (blacklist) {
                if (materials != null && materials.contains(blockType)) continue;
            } else {
                if (materials != null && !materials.contains(blockType)) continue;
            }

            Location location = new Location(world, x + 0.5, y + 1, z + 0.5);

            if (regionRadius!=0) {
                if (!WGHook.isRegionEmpty(regionRadius, location)) {
                    continue;
                }
            }

            return location;
        }
        return null;
    }
}
