package net.craftcitizen.imagemaps;

import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.craftcitizen.imagemaps.clcore.command.HelpCommand;
import net.craftcitizen.imagemaps.clcore.command.SubCommand;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class ImageMapHelpCommand extends HelpCommand {

    public ImageMapHelpCommand(final Plugin plugin, final Map<String, SubCommand> map) {
        super("imagemaps.help", plugin, map);
    }

    @Override
    public void help(final CommandSender sender) {
        if (((ImageMaps) this.getPlugin()).isGlowingSupported()) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                    ImageMapHelpCommand.buildMessage(
                            "/imagemap place <filename> [frameInvisible] [frameFixed] [frameGlowing] [size]",
                            " - starts image placement"));
        } else if (((ImageMaps) this.getPlugin()).isInvisibilitySupported()) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                    ImageMapHelpCommand.buildMessage("/imagemap place <filename> [frameInvisible] [frameFixed] [size]",
                            " - starts image placement"));
        } else {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                    ImageMapHelpCommand.buildMessage("/imagemap place <filename> [size]", " - starts image placement"));
        }

        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                ImageMapHelpCommand.buildMessage("/imagemap download <filename> <sourceURL>", " - downloads an image"));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                ImageMapHelpCommand.buildMessage("/imagemap delete <filename>", " - deletes an image"));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                ImageMapHelpCommand.buildMessage("/imagemap info <filename>", " - displays image info"));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                ImageMapHelpCommand.buildMessage("/imagemap reload <filename>", " - reloads an image from disk"));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                ImageMapHelpCommand.buildMessage("/imagemap cleanup", " - removes invalid maps from plugin"));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                ImageMapHelpCommand.buildMessage("/imagemap list [page]", " - lists all files in the images folder"));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                ImageMapHelpCommand.buildMessage("/imagemap help [command]", " - shows help"));
    }

    private static BaseComponent buildMessage(final String str1, final String str2) {
        final BaseComponent combined = new TextComponent();

        final BaseComponent comp1 = new TextComponent(str1);
        comp1.setColor(ChatColor.WHITE);
        final BaseComponent comp2 = new TextComponent(str2);
        comp2.setColor(ChatColor.GRAY);

        combined.addExtra(comp1);
        combined.addExtra(comp2);

        return combined;
    }
}
