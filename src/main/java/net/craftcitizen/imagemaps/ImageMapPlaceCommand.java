package net.craftcitizen.imagemaps;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import net.craftcitizen.imagemaps.clcore.Utils;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;
import net.craftcitizen.imagemaps.clcore.util.Tuple;

public class ImageMapPlaceCommand extends ImageMapSubCommand {

    public ImageMapPlaceCommand(final ImageMaps plugin) {
        super("imagemaps.place", plugin, false);
    }

    @Override
    protected String execute(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!this.checkSender(sender)) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "You can't run this command.");
            return null;
        }

        if (args.length < 2) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "You must specify a file name.");
            return null;
        }

        final String filename = args[1];
        boolean isInvisible = false;
        boolean isFixed = false;
        boolean isGlowing = false;
        Tuple<Integer, Integer> scale;

        if (this.getPlugin().isInvisibilitySupported()) {
            isInvisible = args.length >= 3 && Boolean.parseBoolean(args[2]);
            isFixed = args.length >= 4 && Boolean.parseBoolean(args[3]);
            if (this.getPlugin().isGlowingSupported()) {
                isGlowing = args.length >= 5 && Boolean.parseBoolean(args[4]);
                scale = args.length >= 6 ? ImageMapPlaceCommand.parseScale(args[5]) : new Tuple<>(-1, -1);
            } else {
                scale = args.length >= 5 ? ImageMapPlaceCommand.parseScale(args[4]) : new Tuple<>(-1, -1);
            }
        } else {
            scale = args.length >= 3 ? ImageMapPlaceCommand.parseScale(args[2]) : new Tuple<>(-1, -1);
        }

        if (filename.contains("/") || filename.contains("\\") || filename.contains(":")) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                    "Filename contains illegal character.");
            return null;
        }

        if (!this.getPlugin().hasImage(filename)) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "No image with this name exists.");
            return null;
        }

        final Player player = (Player) sender;
        player.setMetadata(ImageMaps.PLACEMENT_METADATA,
                new FixedMetadataValue(this.getPlugin(),
                        new PlacementData(filename, isInvisible, isFixed, isGlowing, scale)));

        final Tuple<Integer, Integer> size = this.getPlugin().getImageSize(filename, scale);
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                String.format("Started placing of %s. It needs a %d by %d area.", args[1],
                        size.getKey(), size.getValue()));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Right click on the block, that should be the upper left corner.");
        return null;
    }

    @Override
    public void help(final CommandSender sender) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "Starts placing an image.");

        if (this.getPlugin().isGlowingSupported()) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO,
                    "Usage: /imagemap place <filename> [frameInvisible] [frameFixed] [frameGlowing] [size]");
        } else if (this.getPlugin().isInvisibilitySupported()) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO,
                    "Usage: /imagemap place <filename> [frameInvisible] [frameFixed] [size]");
        } else {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO,
                    "Usage: /imagemap place <filename> [size]");
        }

        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Size format: XxY -> 5x2, use -1 for default");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "The plugin will scale the map to not be larger than the given size while maintaining the aspect ratio.");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "It's recommended to avoid the size function in favor of using properly sized source images.");
    }

    private static Tuple<Integer, Integer> parseScale(final String string) {
        final String[] tmp = string.split("x");

        if (tmp.length < 2)
            return new Tuple<>(-1, -1);

        return new Tuple<>(Utils.parseIntegerOrDefault(tmp[0], -1), Utils.parseIntegerOrDefault(tmp[1], -1));
    }

    @Override
    protected List<String> onTabComplete(final CommandSender sender, final String[] args) {
        if (args.length > 2 && !this.getPlugin().isInvisibilitySupported()
                || args.length > 4 && !this.getPlugin().isGlowingSupported()) {
            return Collections.emptyList();
        }

        return switch (args.length) {
            case 2 -> Utils.getMatches(args[1], new File(this.plugin.getDataFolder(), "images").list());
            case 3 -> Utils.getMatches(args[2], Arrays.asList("true", "false"));
            case 4 -> Utils.getMatches(args[3], Arrays.asList("true", "false"));
            case 5 -> Utils.getMatches(args[4], Arrays.asList("true", "false"));
            default -> Collections.emptyList();
        };
    }
}
