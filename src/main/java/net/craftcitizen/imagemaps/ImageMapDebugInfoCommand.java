package net.craftcitizen.imagemaps;

import javax.imageio.ImageIO;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;

public class ImageMapDebugInfoCommand extends ImageMapSubCommand {

    public ImageMapDebugInfoCommand(final ImageMaps plugin) {
        super("imagemaps.admin", plugin, true);
    }

    @Override
    protected String execute(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "ImageMaps Version " + this.getPlugin().getDescription().getVersion());
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "OS: " + System.getProperty("os.name"));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "ImageIO Params:");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Formats: " + String.join(", ", ImageIO.getReaderFormatNames()));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Suffixes: " + String.join(", ", ImageIO.getReaderFileSuffixes()));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "MIME: " + String.join(", ", ImageIO.getReaderMIMETypes()));
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Uses Cache: " + Boolean.toString(ImageIO.getUseCache()));
        return null;
    }

    @Override
    public void help(final CommandSender sender) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL, "Prints some debug output.");
    }

}
