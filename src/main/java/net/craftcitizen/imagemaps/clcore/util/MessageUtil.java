package net.craftcitizen.imagemaps.clcore.util;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

public class MessageUtil {
    private static final MessageSettings NULL_SETTINGS;
    private static final Map<String, MessageSettings> settings;

    public enum MessageLevel {
        NORMAL,
        ERROR,
        INFO,
        WARNING,
        DEBUG;
    }

    private MessageUtil() {
    }

    public static void registerPlugin(final Plugin plugin, final BaseComponent prefix, final ChatColor normalColor,
            final ChatColor infoColor, final ChatColor warningColor, final ChatColor errorColor,
            final ChatColor debugColor) {
        MessageUtil.settings.put(plugin.getName(),
                new MessageSettings(prefix, normalColor, infoColor, warningColor, errorColor, debugColor));
    }

    public static void sendMessage(final Plugin plugin, final CommandSender sender, final MessageLevel level,
            final String message) {
        sender.spigot()
                .sendMessage(MessageUtil.settings
                        .getOrDefault((plugin != null) ? plugin.getName() : "", MessageUtil.NULL_SETTINGS)
                        .formatMessage(level, message));
    }

    public static void sendMessage(final Plugin plugin, final CommandSender sender, final MessageLevel level,
            final BaseComponent message) {
        sender.spigot()
                .sendMessage(MessageUtil.settings
                        .getOrDefault((plugin != null) ? plugin.getName() : "", MessageUtil.NULL_SETTINGS)
                        .formatMessage(level, message));
    }

    static {
        NULL_SETTINGS = new MessageSettings();
        settings = new HashMap<String, MessageSettings>();
    }

    private static class MessageSettings {
        private final BaseComponent prefix;
        private final Map<MessageLevel, ChatColor> levelColors;

        MessageSettings() {
            this.levelColors = new EnumMap<MessageLevel, ChatColor>(MessageLevel.class);
            this.prefix = (BaseComponent) new TextComponent();
        }

        public MessageSettings(final BaseComponent prefix, final ChatColor normalColor, final ChatColor infoColor,
                final ChatColor warningColor, final ChatColor errorColor, final ChatColor debugColor) {
            this.levelColors = new EnumMap<MessageLevel, ChatColor>(MessageLevel.class);
            this.prefix = prefix;
            this.levelColors.put(MessageLevel.NORMAL, normalColor);
            this.levelColors.put(MessageLevel.INFO, infoColor);
            this.levelColors.put(MessageLevel.WARNING, warningColor);
            this.levelColors.put(MessageLevel.ERROR, errorColor);
            this.levelColors.put(MessageLevel.DEBUG, debugColor);
        }

        public BaseComponent formatMessage(final MessageLevel level, final String message) {
            return this.formatMessage(level, (BaseComponent) new TextComponent(message));
        }

        public BaseComponent formatMessage(final MessageLevel level, final BaseComponent message) {
            final ChatColor levelColor = this.levelColors.get(level);
            final BaseComponent base = (BaseComponent) new TextComponent();
            if (levelColor != null) {
                base.setColor(levelColor);
            }
            base.addExtra(this.prefix);
            base.addExtra(" ");
            base.addExtra(message);
            return base;
        }
    }

}
