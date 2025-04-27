package net.craftcitizen.imagemaps.clcore;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

public class Utils {
    public static final int ELEMENTS_PER_PAGE = 10;
    public static final ChatColor TEXT_COLOR_UNIMPORTANT;
    public static final ChatColor TEXT_COLOR_IMPORTANT;
    public static final String INDENTATION = "  ";
    public static final int MS_PER_MINUTE = 60000;
    public static final int MS_PER_HOUR = 3600000;
    public static final int MS_PER_DAY = 86400000;
    public static final SemanticVersion MC_VERSION;

    private Utils() {
    }

    public static SemanticVersion getMCVersion() {
        return Utils.MC_VERSION;
    }

    public static int parseIntegerOrDefault(final String val, final int defaultVal) {
        try {
            return Integer.parseInt(val);
        } catch (final NumberFormatException e) {
            return defaultVal;
        }
    }

    public static double parseDoubleOrDefault(final String val, final double defaultVal) {
        try {
            return Double.parseDouble(val);
        } catch (final NumberFormatException e) {
            return defaultVal;
        }
    }

    public static float parseFloatOrDefault(final String val, final float defaultVal) {
        try {
            return Float.parseFloat(val);
        } catch (final NumberFormatException e) {
            return defaultVal;
        }
    }

    public static <T> boolean arrayContains(final T[] a, final T o) {
        if (a != null && a.length != 0) {
            for (final T ob : a) {
                if (ob.equals(o)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static List<String> getMatches(final String value, final Collection<String> list) {
        return list.stream().filter(a -> a.toLowerCase().startsWith(value.toLowerCase())).collect(Collectors.toList());
    }

    public static List<String> getMatches(final String value, final String[] list) {
        return Arrays.stream(list).filter(a -> a.toLowerCase().startsWith(value.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static float clamp(final float value, final float min, final float max) {
        return Math.max(Math.min(value, max), min);
    }

    public static double clamp(final double value, final double min, final double max) {
        return Math.max(Math.min(value, max), min);
    }

    public static String ticksToTimeString(final long ticks) {
        final long h = ticks / 72000L;
        final long min = ticks / 1200L % 60L;
        final long s = ticks / 20L % 60L;
        return String.format("%dh %02dmin %02ds", h, min, s);
    }

    public static boolean isBetween(final int locX, final int x, final int x2) {
        return (x > x2 && locX >= x2 && locX <= x) || (x < x2 && locX <= x2 && locX >= x);
    }

    public static boolean isBetween(final double d, final double x, final double x2) {
        return (x > x2 && d >= x2 && d <= x) || (x < x2 && d <= x2 && d >= x);
    }

    public static <T> List<T> paginate(final Stream<T> values, final long page) {
        return values.skip(page * 10L).limit(10L).collect(Collectors.toList());
    }

    public static <T> List<T> paginate(final Collection<T> values, final long page) {
        return Utils.paginate(values.stream(), page);
    }

    public static <T> List<T> paginate(final T[] values, final long page) {
        return Utils.paginate((Stream<T>) Arrays.stream((T[]) values), page);
    }

    public static BoundingBox calculateBoundingBoxBlock(final Collection<Block> blocks) {
        final int minX = blocks.stream().map(Block::getX)
                .min(Integer::compare).orElse(0);
        final int maxX = blocks.stream().map(Block::getX)
                .max(Integer::compare).orElse(0);
        final int minY = blocks.stream().map(Block::getY)
                .min(Integer::compare).orElse(0);
        final int maxY = blocks.stream().map(Block::getY)
                .max(Integer::compare).orElse(0);
        final int minZ = blocks.stream().map(Block::getZ)
                .min(Integer::compare).orElse(0);
        final int maxZ = blocks.stream().map(Block::getZ)
                .max(Integer::compare).orElse(0);
        return new BoundingBox((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY,
                (double) maxZ);
    }

    public static BoundingBox calculateBoundingBoxLocation(final Collection<Location> blocks) {
        final int minX = blocks.stream().map(Location::getBlockX)
                .min(Integer::compare).orElse(0);
        final int maxX = blocks.stream().map(Location::getBlockX)
                .max(Integer::compare).orElse(0);
        final int minY = blocks.stream().map(Location::getBlockY)
                .min(Integer::compare).orElse(0);
        final int maxY = blocks.stream().map(Location::getBlockY)
                .max(Integer::compare).orElse(0);
        final int minZ = blocks.stream().map(Location::getBlockZ)
                .min(Integer::compare).orElse(0);
        final int maxZ = blocks.stream().map(Location::getBlockZ)
                .max(Integer::compare).orElse(0);
        return new BoundingBox((double) minX, (double) minY, (double) minZ, (double) maxX, (double) maxY,
                (double) maxZ);
    }

    public static Collection<String> toString(final Collection<?> input) {
        return input.stream().map(Object::toString).collect(Collectors.toList());
    }

    public static Collection<String> toString(final Object[] values) {
        return Arrays.stream(values).map(Object::toString).collect(Collectors.toList());
    }

    public static boolean isChunkLoaded(final Location loc) {
        return loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }

    public static boolean isChunkLoaded(final World world, final int chunkX, final int chunkZ) {
        return world.isChunkLoaded(chunkX, chunkZ);
    }

    public static ItemStack buildItemStack(final Material type, final String name, final List<String> lore) {
        final ItemStack item = new ItemStack(type);
        final ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (!lore.isEmpty()) {
            meta.setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

    static {
        TEXT_COLOR_UNIMPORTANT = ChatColor.GRAY;
        TEXT_COLOR_IMPORTANT = ChatColor.WHITE;
        MC_VERSION = SemanticVersion.of(Bukkit.getBukkitVersion().split("-")[0]);
    }
}
