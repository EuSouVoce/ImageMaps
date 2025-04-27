package net.craftcitizen.imagemaps;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import net.craftcitizen.imagemaps.clcore.util.MessageUtil;
import net.craftcitizen.imagemaps.clcore.util.MessageUtil.MessageLevel;

public class ImageMapCleanupCommand extends ImageMapSubCommand {

    public ImageMapCleanupCommand(final ImageMaps plugin) {
        super("imagemaps.admin", plugin, true);
    }

    @Override
    protected String execute(final CommandSender sender, final Command cmd, final String label, final String[] args) {
        if (!this.checkSender(sender)) {
            MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING, "You can't run this command.");
            return null;
        }

        final int removedMaps = this.getPlugin().cleanupMaps();
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Removed " + removedMaps + " invalid images/maps.");
        return null;
    }

    @Override
    public void help(final CommandSender sender) {
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.NORMAL,
                "Removes maps with invalid IDs or missing image files.");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.WARNING,
                "This action is not reverseable. It is recommended to create a backup of your maps.yml first!");
        MessageUtil.sendMessage(this.getPlugin(), sender, MessageLevel.INFO, "Usage: /imagemap cleanup");
    }

}
