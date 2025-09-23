package me.jetby.treex.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.*;
import com.sk89q.worldguard.protection.flags.registry.FlagRegistry;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static me.jetby.treex.Treex.LOGGER;


@UtilityClass
public class WGHook {

    private boolean initialized = false;

    public void init() {
        WorldGuardPlugin worldGuardPlugin = (WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard");
        if (worldGuardPlugin != null) {
            initialized = true;
            LOGGER.info("WorldGuard hook initialized.");
        } else {
            LOGGER.error("WorldGuard plugin not found. WorldGuard hook not initialized.");
        }
    }

    public boolean isRegionEmpty(int radius, @NotNull Location location) {
        if (!initialized) {
            LOGGER.error("WorldGuard is not initialized.");
            return false;
        }
        if (location.getWorld() == null) return false;
        try {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get((BukkitAdapter.adapt(location.getWorld())));

            ProtectedCuboidRegion region = new ProtectedCuboidRegion(UUID.randomUUID() + "_region",
                    BlockVector3.at(location.getBlockX() + radius, location.getBlockY() + radius, location.getBlockZ() + radius),
                    BlockVector3.at(location.getBlockX() - radius, location.getBlockY() - radius, location.getBlockZ() - radius));

            if (regions==null) return true;
            Map<String, ProtectedRegion> rg = regions.getRegions();
            List<ProtectedRegion> candidates = new ArrayList<>(rg.values());

            List<ProtectedRegion> overlapping = region.getIntersectingRegions(candidates);

            return overlapping.isEmpty();
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }

    }

    public void createRegion(@NotNull Location location, String prefix, int privateRadius, List<String> flags) {
        if (!initialized) {
            LOGGER.error("WorldGuard is not initialized.");
            return;
        }
        World world = location.getWorld();
        if (world == null) return;
        WorldGuard wg = WorldGuard.getInstance();
        if (wg == null) return;
        RegionManager regionManager = wg.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));

        if (regionManager != null) {
            ProtectedCuboidRegion region = getProtectedCuboidRegion(location, prefix, privateRadius);

            applyAllFlags(region, flags);

            regionManager.addRegion(region);
            try {
                regionManager.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private ProtectedCuboidRegion getProtectedCuboidRegion(@NotNull Location location, String prefix, int regionRadius) {
        if (!initialized) {
            LOGGER.error("WorldGuard is not initialized.");
            return null;
        }
        BlockVector3 min = BlockVector3.at(location.getBlockX() - regionRadius,
                location.getBlockY() - regionRadius,
                location.getBlockZ() - regionRadius);
        BlockVector3 max = BlockVector3.at(location.getBlockX() + regionRadius,
                location.getBlockY() + regionRadius,
                location.getBlockZ() + regionRadius);

        return new ProtectedCuboidRegion(
                prefix + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ(),
                min, max);
    }

    private void applyAllFlags(@NotNull ProtectedRegion region, @NotNull List<String> flags) {
        if (!initialized) {
            LOGGER.error("WorldGuard is not initialized.");
            return;
        }
        if (flags.isEmpty()) {
            return;
        }

        FlagRegistry registry = WorldGuard.getInstance().getFlagRegistry();

        for (String flagEntry : flags) {
            String[] parts = flagEntry.split(" ", 2);
            if (parts.length < 2) continue;

            String flagName = parts[0].toLowerCase();
            String value = parts[1];

            try {
                Flag<?> flag = registry.get(flagName);
                if (flag == null) {
                    flag = registry.get("flag-" + flagName);
                }

                if (flag != null) {
                    setFlagValue(region, flag, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private <T> void setFlagValue(ProtectedRegion region, Flag<T> flag, String value) {
        if (!initialized) {
            LOGGER.error("WorldGuard is not initialized.");
            return;
        }
        try {
            if (flag instanceof StateFlag) {
                StateFlag.State state = StateFlag.State.valueOf(value.toUpperCase());
                region.setFlag((StateFlag) flag, state);
            } else if (flag instanceof StringFlag) {
                region.setFlag((StringFlag) flag, value);
            } else if (flag instanceof IntegerFlag) {
                region.setFlag((IntegerFlag) flag, Integer.parseInt(value));
            } else if (flag instanceof DoubleFlag) {
                region.setFlag((DoubleFlag) flag, Double.parseDouble(value));
            } else if (flag instanceof BooleanFlag) {
                region.setFlag((BooleanFlag) flag, Boolean.parseBoolean(value));
            } else if (flag instanceof SetFlag) {
                @SuppressWarnings("unchecked")
                SetFlag<String> stringSetFlag = (SetFlag<String>) flag;

                Set<String> set = new HashSet<>();
                for (String cmd : value.split(",")) {
                    set.add(cmd.trim());
                }
                region.setFlag(stringSetFlag, set);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeRegion(Location location, String prefix) {
        if (!initialized) {
            LOGGER.error("WorldGuard is not initialized.");
            return;
        }
        if (location == null) return;
        World world = location.getWorld();
        if (world == null) return;
        WorldGuard wg = WorldGuard.getInstance();
        if (wg == null) return;
        RegionManager regionManager = wg.getPlatform().getRegionContainer().get(BukkitAdapter.adapt(world));
        if (regionManager == null) return;

        if (regionManager.hasRegion(prefix + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ())) {
            regionManager.removeRegion(prefix + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
            try {
                regionManager.save();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isInRegion(Location location, String prefix) {
        if (!initialized) {
            LOGGER.error("WorldGuard is not initialized.");
            return false;
        }
        if (location == null || location.getWorld() == null) return false;
        com.sk89q.worldedit.world.World weWorld = BukkitAdapter.adapt(location.getWorld());
        Location wgLocation = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        BlockVector3 blockVector = BlockVector3.at(wgLocation.getX(), wgLocation.getY(), wgLocation.getZ());
        RegionContainer regionContainer = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regionManager = regionContainer.get(weWorld);
        if (regionManager != null) {
            ApplicableRegionSet regions = regionManager.getApplicableRegions(blockVector);
            ProtectedRegion highestPriorityRegion = null;
            for (ProtectedRegion region : regions) {
                if (highestPriorityRegion == null || region.getPriority() > highestPriorityRegion.getPriority())
                    highestPriorityRegion = region;
            }
            return highestPriorityRegion != null && highestPriorityRegion.getId().startsWith(prefix);
        }
        return false;
    }

}
