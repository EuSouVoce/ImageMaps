package net.craftcitizen.imagemaps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.craftcitizen.imagemaps.clcore.Utils;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;
import net.craftcitizen.imagemaps.clcore.util.Tuple;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ImageMapInfoCommand extends ImageMapSubCommand {

    public ImageMapInfoCommand(final ImageMaps plugin) {
        super("imagemaps.info", plugin, true);
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
        final BufferedImage image = this.getPlugin().getImage(filename);

        if (image == null) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "No image with this name exists.");
            return null;
        }

        final Tuple<Integer, Integer> size = this.getPlugin().getImageSize(filename, null);
        final BaseComponent reloadAction = new TextComponent("[Reload]");
        reloadAction.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                String.format("/imagemap reload \"%s\"", filename)));
        reloadAction.setColor(ChatColor.GOLD);
        final BaseComponent placeAction = new TextComponent("[Place]");
        placeAction.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                String.format("/imagemap place \"%s\"", filename)));
        placeAction.setColor(ChatColor.GOLD);
        final BaseComponent deleteAction = new TextComponent("[Delete]");
        deleteAction.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                String.format("/imagemap delete \"%s\"", filename)));
        deleteAction.setColor(ChatColor.RED);

        final BaseComponent actions = new TextComponent("Action: ");
        actions.addExtra(reloadAction);
        actions.addExtra(" ");
        actions.addExtra(placeAction);
        actions.addExtra(" ");
        actions.addExtra(deleteAction);

        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO, "Image Information: ");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                String.format("File Name: %s", filename));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                String.format("Resolution: %dx%d", image.getWidth(), image.getHeight()));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                String.format("Ingame Size: %dx%d", size.getKey(), size.getValue()));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, actions);
        return null;
    }

    @Override
    public void help(final CommandSender sender) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "Displays information about an image.");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO, "Usage: /imagemap info <filename>");
    }

    @Override
    protected List<String> onTabComplete(final CommandSender sender, final String[] args) {
        if (args.length == 2)
            return Utils.getMatches(args[1], new File(this.plugin.getDataFolder(), "images").list());

        return Collections.emptyList();
    }
}
