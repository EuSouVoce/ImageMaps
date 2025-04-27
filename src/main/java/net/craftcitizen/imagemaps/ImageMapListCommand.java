package net.craftcitizen.imagemaps;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.craftcitizen.imagemaps.clcore.Utils;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

public class ImageMapListCommand extends ImageMapSubCommand {

    public ImageMapListCommand(final ImageMaps plugin) {
        super("imagemaps.list", plugin, true);
    }

    @Override
    protected String execute(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!this.checkSender(sender)) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "You can't run this command.");
            return null;
        }

        final String[] fileList = new File(this.plugin.getDataFolder(), "images").list();
        final long page = args.length >= 2 ? Utils.parseIntegerOrDefault(args[1], 0) - 1 : 0;
        final int numPages = (int) Math.ceil((double) fileList.length / Utils.ELEMENTS_PER_PAGE);

        MessageUtil.sendMessage(this.plugin, sender, MessageLevel.INFO,
                String.format("## Image List Page %d of %d ##", page + 1, numPages));

        boolean even = false;
        for (final String filename : Utils.paginate(fileList, page)) {
            final BaseComponent infoAction = new TextComponent("[Info]");
            infoAction.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                    String.format("/imagemap info \"%s\"", filename)));
            infoAction.setColor(ChatColor.GOLD);
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

            final BaseComponent message = new TextComponent(filename);
            message.setColor(even ? ChatColor.GRAY : ChatColor.WHITE);
            message.addExtra(" ");
            message.addExtra(infoAction);
            message.addExtra(" ");
            message.addExtra(reloadAction);
            message.addExtra(" ");
            message.addExtra(placeAction);
            message.addExtra(" ");
            message.addExtra(deleteAction);

            MessageUtil.sendMessage(this.plugin, sender, MessageLevel.NORMAL, message);
            even = !even;
        }

        final BaseComponent navigation = new TextComponent();
        final BaseComponent prevPage = new TextComponent(String.format("<< Page %d", Math.max(page, 1)));
        final BaseComponent nextPage = new TextComponent(String.format("Page %d >>", Math.min(page + 2, numPages)));
        prevPage.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/imagemap list " + Math.max(page, 1)));
        nextPage.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/imagemap list " + Math.min(page + 2, numPages)));

        navigation.addExtra(prevPage);
        navigation.addExtra(" | ");
        navigation.addExtra(nextPage);
        MessageUtil.sendMessage(this.plugin, sender, MessageLevel.INFO, navigation);
        return null;
    }

    @Override
    public void help(final CommandSender sender) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "Lists all files in the images folder.");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO, "Usage: /imagemap list [page]");
    }
}
