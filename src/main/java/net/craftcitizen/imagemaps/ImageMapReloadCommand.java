package net.craftcitizen.imagemaps;

import java.io.File;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.craftcitizen.imagemaps.clcore.Utils;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;

public class ImageMapReloadCommand extends ImageMapSubCommand {

    public ImageMapReloadCommand(final ImageMaps plugin) {
        super("imagemap.reload", plugin, true);
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

        if (filename.contains("/") || filename.contains("\\") || filename.contains(":")) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                    "Filename contains illegal character.");
            return null;
        }

        if (this.getPlugin().reloadImage(filename))
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "Image reloaded.");
        else
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                    "Image couldn't be reloaded (does it exist?).");

        return null;
    }

    @Override
    public void help(final CommandSender sender) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Reloads an image from disk, to be used when the file changed.");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Avoid resolution changes, since they won't be scaled.");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO, "Usage: /imagemap reload <filename>");
    }

    @Override
    protected List<String> onTabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 2)
            return Utils.getMatches(args[1], new File(this.plugin.getDataFolder(), "images").list());

        return Collections.emptyList();
    }

}
